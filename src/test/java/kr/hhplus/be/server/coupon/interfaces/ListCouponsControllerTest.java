package kr.hhplus.be.server.coupon.interfaces;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.interfaces.dto.response.ListCouponsResponse;
import kr.hhplus.be.server.mock.TestContainers;
import kr.hhplus.be.server.mock.domain.CouponFixture;
import kr.hhplus.be.server.mock.domain.IssuedCouponFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("컨트롤러 단위 테스트 : 사용자 쿠폰 목록 조회")
class ListCouponsControllerTest {

    @DisplayName("""
            보유 쿠폰이 3개인 사용자가 존재할 때,
            
            사용자 쿠폰 목록을 요청하면,
            
            {200 OK 응답}과
            {쿠폰 정보 목록}을 응답해야 한다.
            """)
    @Test
    void getListCoupons() {

        // Mocking
        TestContainers container = new TestContainers();
        ListCouponsController sut = container.listCouponsController;

        // given : 보유 쿠폰이 3개인 사용자가 존재
        Coupon coupon = CouponFixture.create(1L);
        IssuedCoupon issuedCoupon1 = IssuedCouponFixture.create(1L, coupon, 1L);
        IssuedCoupon issuedCoupon2 = IssuedCouponFixture.create(2L, coupon, 1L);
        IssuedCoupon issuedCoupon3 = IssuedCouponFixture.create(3L, coupon, 1L);
        List<IssuedCoupon> issuedCoupons = List.of(issuedCoupon1, issuedCoupon2, issuedCoupon3);
        container.couponRepository.saveAll(issuedCoupons);

        // when : 사용자 쿠폰 목록을 요청하면
        ResponseEntity<List<ListCouponsResponse>> responses = sut.listCoupons(1L);

        // then 1 : 200 OK 응답
        assertThat(responses.getStatusCode()).isEqualTo(HttpStatus.OK);

        // then 2 : 응답 바디에는 쿠폰 정보 목록이 포함되어야 한다.
        for (int i = 0; i < responses.getBody().size(); i++) {
            ListCouponsResponse listCouponsResponse = responses.getBody().get(i);
            IssuedCoupon issuedCoupon = issuedCoupons.get(i);
            assertThat(listCouponsResponse.couponId()).isEqualTo(issuedCoupon.getCouponId().toString());
            assertThat(listCouponsResponse.couponName()).isEqualTo(issuedCoupon.getCouponName());
            assertThat(listCouponsResponse.discountAmount()).isEqualTo(issuedCoupon.getDiscountAmount().toLong());
            assertThat(listCouponsResponse.discountAmount()).isEqualTo(issuedCoupon.getDiscountAmount().toLong());
            assertThat(listCouponsResponse.expiredAt()).isEqualTo(issuedCoupon.getExpiredAt());
        }
    }

}