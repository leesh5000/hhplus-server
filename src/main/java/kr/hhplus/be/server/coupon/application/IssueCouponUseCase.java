package kr.hhplus.be.server.coupon.application;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class IssueCouponUseCase {

    private final UserService userService;
    private final CouponService couponService;

    public void issue(Long userId, Long couponId) {
        User user = userService.getById(userId);
        couponService.issueCoupon(couponId, user.getId());
    }
}
