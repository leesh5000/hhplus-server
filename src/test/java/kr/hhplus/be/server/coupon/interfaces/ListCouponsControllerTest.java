package kr.hhplus.be.server.coupon.interfaces;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.interfaces.dto.response.ListCouponsResponse;
import kr.hhplus.be.server.mock.TestContainer;
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

    @DisplayName("`userId`를 전달받아 " +
            "사용자 쿠폰 목록을 요청하면, " +
            "200 응답과 " +
            "쿠폰 정보 목록을 반환해야 한다.")
    @Test
    void getListCoupons() {

        // Mocking
        TestContainer container = new TestContainer();
        Coupon couponFixture = CouponFixture.create(1L);
        IssuedCoupon issuedCoupon1 = IssuedCouponFixture.create(1L, couponFixture, 1L);
        IssuedCoupon issuedCoupon2 = IssuedCouponFixture.create(2L, couponFixture, 1L);
        IssuedCoupon issuedCoupon3 = IssuedCouponFixture.create(3L, couponFixture, 1L);
        List<IssuedCoupon> issuedCoupons = List.of(issuedCoupon1, issuedCoupon2, issuedCoupon3);
        container.couponRepository.saveAll(issuedCoupons);
        ListCouponsController sut = container.listCouponsController;

        // given
        Long userId = 1L;

        // when
        ResponseEntity<List<ListCouponsResponse>> responses = sut.listCoupons(1L);

        // then 1 : 응답 코드가 200이어야 하고, 개수가 3개 여야한다.
        assertThat(responses.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responses.getBody()).isNotNull();
        assertThat(responses.getBody().size()).isEqualTo(3);
        // then 2 : 응답 바디에는 쿠폰 정보 목록이 포함되어야 한다.
        for (int i = 0; i < responses.getBody().size(); i++) {
            assertThat(responses.getBody().get(i).couponId()).isEqualTo(issuedCoupons.get(i).getCouponId().toString());
            assertThat(responses.getBody().get(i).couponName()).isEqualTo(issuedCoupons.get(i).getCouponName());
            assertThat(responses.getBody().get(i).discountAmount()).isEqualTo(issuedCoupons.get(i).getDiscountAmount().toLong());
            assertThat(responses.getBody().get(i).discountAmount()).isEqualTo(issuedCoupons.get(i).getDiscountAmount().toLong());
            assertThat(responses.getBody().get(i).expiredAt()).isEqualTo(issuedCoupons.get(i).getExpiredAt());
        }
    }

}