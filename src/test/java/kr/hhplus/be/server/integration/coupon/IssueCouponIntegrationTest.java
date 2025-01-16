package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API 통합 테스트 : 쿠폰 발급")
@AutoConfigureMockMvc
@SpringBootTest
public class IssueCouponIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CouponService couponService;

    @DisplayName("""
            ID가 1인 유저와 ID가 1인 쿠폰이 존재할 때,
            
            쿠폰 발급 요청을 하면,
            
            {201 Created} 응답과
            Location 헤더에 {발급된 쿠폰 URI}가 포함되어야 하고,
            {쿠폰 재고가 1 감소}해야 한다.
            """)
    @Test
    void issue_coupon_test_1() throws Exception {

        // given : ID가 1인 유저와 ID가 1인 쿠폰이 존재 (resources/sql/data.sql 참고)
        Coupon coupon = couponService.getByIdWithPessimisticLock(1L);
        int stock = coupon.getStock();

        // when : 쿠폰 발급 요청을 하면
        mockMvc.perform(
                        post("/api/v1/users/1/coupons/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // then 1 : 201 Created 응답해야 한다.
                .andExpect(status().isCreated())
                // then 2 : Location 헤더에 사용자 쿠폰 목록 URI 가 포함되어야 한다.
                .andExpect(header().string("Location", "http://localhost/api/v1/users/1/coupons/1"));

        // then 3 : 쿠폰 재고가 1 감소해야 한다.
        Coupon updatedCoupon = couponService.getByIdWithPessimisticLock(1L);
        int updatedStock = updatedCoupon.getStock();
        Assertions.assertThat(updatedStock).isEqualTo(stock - 1);
    }

    @DisplayName("""
            재고가 0인 쿠폰이 존재할 때,

            쿠폰 발급 요청을 하면,

            {409 CONFLICT} 응답과
            응답 본문 중
            {errorCode} 가 {COUPON_STOCK_EXHAUSTED} 이고,
            {message} 가 {"쿠폰이 모두 소진되었습니다."} 여야 한다.
            """)
    @SqlGroup({
            @Sql(value = "/sql/insert_zero_stock_coupon.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete_zero_stock_coupon.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void issue_zero_stock_coupon_test() throws Exception {

        // given : 재고가 0인 쿠폰이 존재 (resources/sql/insert_zero_stock_coupon.sql 참고)
        Long zeroStockCouponId = 100L;

        // when : 쿠폰 발급 요청을 하면
        mockMvc.perform(
                        post("/api/v1/users/1/coupons/{couponId}", zeroStockCouponId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // then 1 : 409 CONFLICT 응답해야 한다.
                .andExpect(status().isConflict())
                // then 2 : errorCode 가 `COUPON_STOCK_EXHAUSTED`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.errorCode").value("COUPON_STOCK_EXHAUSTED"))
                // then 3 : message 가 `재고가 소진된 쿠폰입니다.`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.message").value("쿠폰이 모두 소진되었습니다."));
    }

    @DisplayName("""
            유효기간이 만료된 쿠폰이 존재할 때,
            
            쿠폰 발급 요청을 하면,
            
            {400 Bad Request} 응답과
            응답 본문 중
            {errorCode} 가 {EXPIRED_COUPON} 이고,
            {message} 가 {"쿠폰이 만료되었습니다."} 여야 한다.
            {쿠폰 재고는 그대로}여야 한다.
            """)
    @SqlGroup({
            @Sql(value = "/sql/insert_expired_coupon.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete_expired_coupon.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void issue_expired_coupon_test() throws Exception {

        // given : 유효기간이 만료된 쿠폰이 존재 (resources/sql/insert_expired_coupon.sql 참고)
        Long expiredCouponId = 100L;
        Coupon coupon = couponService.getByIdWithPessimisticLock(expiredCouponId);
        int stock = coupon.getStock();

        // when : 쿠폰 발급 요청을 하면
        mockMvc.perform(
                        post("/api/v1/users/1/coupons/{couponId}", expiredCouponId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // then 1 : 400 Bad Request 응답해야 한다.
                .andExpect(status().isBadRequest())
                // then 2 : errorCode 가 `EXPIRED_COUPON`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.errorCode").value("EXPIRED_COUPON"))
                // then 3 : message 가 `쿠폰이 만료되었습니다.`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.message").value("이미 만료일이 지난 쿠폰입니다."));

        // then 4 : 쿠폰 재고는 그대로여야 한다.
        Coupon updatedCoupon = couponService.getByIdWithPessimisticLock(expiredCouponId);
        assertEquals(stock, updatedCoupon.getStock());
    }

    @DisplayName("""
    동시에 30명의 사용자가 재고가 50개인 쿠폰을
    발급 요청할 때,
    {201 Created} 응답과
    정확히 {쿠폰 재고가 30 감소}해야 하고,
    {30명의 유저 모두 쿠폰을 발급}받아야 한다.
    """)
    @SqlGroup({
            @Sql(value = "/sql/insert_coupon_concurrency_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete_coupon_concurrency_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void issue_coupon_concurrently_test() throws Exception {
        // 동시 실행할 쓰레드 개수 (유저 수)
        int userCount = 30;

        // 동시 실행 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(userCount);

        // 병렬로 실행될 작업 목록
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 100; i < userCount + 100; i++) {
            final int userId = i + 1; // 1 ~ 30
            tasks.add(() -> {
                // mockMvc 호출 -> 성공 여부를 반환
                mockMvc.perform(
                        post("/api/v1/users/{userId}/coupons/{couponId}", userId, 101)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated());
                return true;  // 성공했다고 가정
            });
        }

        // 모든 작업(30개)을 동시에 실행
        executorService.invokeAll(tasks);
        executorService.shutdown();

        // 이후 쿠폰 재고(예: 50 -> 20)로 잘 감소했는지, 30명이 정상 발급받았는지 등의 검증 로직
        // 보통 CouponRepository, CouponInventoryRepository 등을 통해 DB 조회 후 assert
        int expectedStock = 20; // 50 - 30
        int actualStock = couponService.getByIdWithPessimisticLock(101L).getStock();
        assertThat(actualStock).isEqualTo(expectedStock);
    }



}
