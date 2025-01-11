package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.coupon.domain.Coupon;

import java.time.LocalDateTime;

public class CouponFixture {

    private CouponFixture() {
        // do nothing
    }

    public static Coupon create(Long id) {
        LocalDateTime expiredAt = LocalDateTime.now()
                .plusMonths(3);
        return new Coupon(id,
                "쿠폰",
                1000L,
                100,
                expiredAt
                );
    }
}
