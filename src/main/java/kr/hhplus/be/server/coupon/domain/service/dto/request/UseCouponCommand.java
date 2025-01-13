package kr.hhplus.be.server.coupon.domain.service.dto.request;

import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.user.domain.User;

import java.util.List;

public record UseCouponCommand(
        User user,
        Payment payment,
        List<IssuedCoupon> issuedCoupons
) {
}
