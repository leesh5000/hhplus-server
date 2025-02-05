package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.coupon.domain.Coupon;

import java.time.LocalDateTime;

public class CouponFixture {

    private CouponFixture() {
        // do nothing
    }

    public static Coupon create(LocalDateTime expiredAt) {
        return new Coupon(null,
                "쿠폰",
                1000L,
                100,
                expiredAt
        );
    }

    public static Coupon create(Integer stock) {
        LocalDateTime expiredAt = LocalDateTime.now()
                .plusMonths(3);
        return new Coupon(null,
                "쿠폰",
                1000L,
                stock,
                expiredAt
        );
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

    public static Coupon create(Long id, Integer stock) {
        LocalDateTime expiredAt = LocalDateTime.now()
                .plusMonths(3);
        return new Coupon(id,
                "쿠폰",
                1000L,
                stock,
                expiredAt
        );
    }

    public static Coupon create(Long id, LocalDateTime expiredAt) {
        return new Coupon(id,
                "쿠폰",
                1000L,
                100,
                expiredAt
        );
    }
}
