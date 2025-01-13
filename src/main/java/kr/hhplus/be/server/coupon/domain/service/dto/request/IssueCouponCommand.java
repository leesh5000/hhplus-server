package kr.hhplus.be.server.coupon.domain.service.dto.request;

import kr.hhplus.be.server.user.domain.User;

public record IssueCouponCommand(
        User user,
        Long couponId
) {
}
