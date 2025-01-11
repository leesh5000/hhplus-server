package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;

public class IssuedCouponFixture {

    private IssuedCouponFixture() {
        // do nothing
    }

    public static IssuedCoupon create(Long id, Coupon coupon, Long userId) {
        return new IssuedCoupon(
                id,
                userId,
                coupon
        );
    }

    public static IssuedCoupon create(Long id, Coupon coupon) {
        return new IssuedCoupon(
                id,
                1L,
                coupon
        );
    }
}
