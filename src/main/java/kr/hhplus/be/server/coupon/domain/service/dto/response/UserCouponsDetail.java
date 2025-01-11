package kr.hhplus.be.server.coupon.domain.service.dto.response;

import kr.hhplus.be.server.coupon.domain.IssuedCoupon;

import java.time.LocalDateTime;

public record UserCouponsDetail(
        Long couponId,
        String couponName,
        Long discountAmount,
        Boolean used,
        LocalDateTime usedAt,
        LocalDateTime expiredAt,
        LocalDateTime receivedAt
) {
    public static UserCouponsDetail from(IssuedCoupon issuedCoupon) {
        return new UserCouponsDetail(
                issuedCoupon.getCouponId(),
                issuedCoupon.getCouponName(),
                issuedCoupon.getDiscountAmount().amount(),
                issuedCoupon.isUsed(),
                issuedCoupon.getUsedAt(),
                issuedCoupon.getExpiredAt(),
                issuedCoupon.getIssuedAt()
        );
    }
}
