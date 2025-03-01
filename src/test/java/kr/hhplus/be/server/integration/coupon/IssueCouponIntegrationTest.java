package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.domain.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserService userService;

    @DisplayName("쿠폰을 발급 요청하면, 정상적으로 유저에게 발급이 되고 쿠폰 재고가 1 감소해야 한다.")
    @Test
    void issue_coupon_test_1() throws Exception {

        long userId = 1L;
        long couponId = 1L;
        User user = userService.getById(userId);
        Integer userCouponCount = user.getMyCoupons(couponId)
                .size();
        Coupon coupon = couponService.getById(couponId);
        int stock = coupon.getStock();

        // when
        mockMvc.perform(
                        post("/api/v1/users/{userId}/coupons/{couponId}", 1, 1)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/users/1/coupons/1"));

        // then
        Coupon updatedCoupon = couponService.getById(userId);
        int updatedStock = updatedCoupon.getStock();
        Integer updatedUserCouponCount = user.getMyCoupons(couponId)
                .size();
        Assertions.assertThat(updatedStock).isEqualTo(stock - 1);
        Assertions.assertThat(updatedUserCouponCount).isEqualTo(userCouponCount + 1);
    }

    @DisplayName("""
            재고가 0인 쿠폰이 존재할 때,
            
            쿠폰 발급 요청을 하면,
            
            {409 CONFLICT} 응답과
            응답 본문 중
            {errorCode} 가 {COUPON_STOCK_EXHAUSTED} 이고,
            {message} 가 {"쿠폰이 모두 소진되었습니다."} 여야 한다.
            """)
    @Test
    void issue_zero_stock_coupon_test() throws Exception {

        // when : 쿠폰 발급 요청을 하면
        mockMvc.perform(post("/api/v1/users/1/coupons/{couponId}", 100).contentType(MediaType.APPLICATION_JSON))
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
    @Test
    void issue_expired_coupon_test() throws Exception {

        Long expiredCouponId = 101L;
        Coupon coupon = couponService.getById(expiredCouponId);
        int stock = coupon.getStock();

        // when : 쿠폰 발급 요청을 하면
        mockMvc.perform(
                        post("/api/v1/users/1/coupons/{couponId}", expiredCouponId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // then 1 : 400 Bad Request 응답해야 한다.
                .andExpect(status().isConflict())
                // then 2 : errorCode 가 `EXPIRED_COUPON`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.errorCode").value("EXPIRED_COUPON"))
                // then 3 : message 가 `쿠폰이 만료되었습니다.`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.message").value("이미 만료일이 지난 쿠폰입니다."));

        // then 4 : 쿠폰 재고는 그대로여야 한다.
        Coupon updatedCoupon = couponService.getById(expiredCouponId);
        assertEquals(stock, updatedCoupon.getStock());
    }

    @DisplayName("""
    동시에 30명의 사용자가 재고가 50개인 쿠폰을
    발급 요청할 때,
    {201 Created} 응답과
    정확히 {쿠폰 재고가 30 감소}해야 하고,
    {30명의 유저 모두 쿠폰을 발급}받아야 한다.
    """)
    @Test
    void issue_coupon_concurrently_test() throws Exception {

        // 동시 실행 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(30);

        // 병렬로 실행될 작업 목록
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            final int userId = i;
            tasks.add(() -> {
                // mockMvc 호출 -> 성공 여부를 반환
                mockMvc.perform(
                        post("/api/v1/users/{userId}/coupons/{couponId}", userId, 102)
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
        int actualStock = couponService.getById(102L).getStock();
        assertThat(actualStock).isEqualTo(expectedStock);
    }

    @DisplayName("""
            동시에 30명의 사용자가 재고가 10개인 쿠폰을
            발급 요청하면,
            10명의 유저만 쿠폰을 발급받아야 한다.
            """)
    @Test
    void issue_coupon_concurrently_test_2() throws Exception {

        long tenStockCouponId = 103L;
        int userCount = 30;

        // 동시 실행 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(userCount);

        // 병렬로 실행될 작업 목록
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 1; i <= userCount; i++) {
            final int userId = i;
            tasks.add(() -> {
                try {
                    // mockMvc 호출 -> 성공 여부를 반환
                    mockMvc.perform(post("/api/v1/users/{userId}/coupons/{couponId}", userId, tenStockCouponId)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(
                                    status().isCreated());
                } catch (Exception e) {
                    // ignored
                }
                return true;  // 성공했다고 가정
            });
        }

        // 모든 작업을 동시에 실행
        executorService.invokeAll(tasks);
        executorService.shutdown();

        int expectedStock = 0;
        int actualStock = couponService.getById(tenStockCouponId).getStock();
        assertThat(actualStock).isEqualTo(expectedStock);

        AtomicInteger totalIssuedCouponCount = new AtomicInteger();
        for (int i = 1; i <= userCount; i++) {

            List<IssuedCoupon> issuedCouponsByUserId = couponRepository.findIssuedCouponsByUserId((long) i)
                    .stream()
                    .filter(issuedCoupon -> issuedCoupon.getCouponId() == tenStockCouponId)
                    .toList();

            if (issuedCouponsByUserId.isEmpty()) {
                continue;
            }

            int size = issuedCouponsByUserId.size();
            Assertions.assertThat(size).isEqualTo(1);
            totalIssuedCouponCount.addAndGet(size);
        }

        assertThat(totalIssuedCouponCount.get()).isEqualTo(10);
    }

}
