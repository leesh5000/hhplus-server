package kr.hhplus.be.server.coupon.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.common.domain.service.ClockHolder;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.service.dto.request.IssueCouponCommand;
import kr.hhplus.be.server.coupon.domain.service.dto.request.UseCouponCommand;
import kr.hhplus.be.server.coupon.domain.service.dto.response.UserCouponsDetail;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final ClockHolder clockHolder;

    public List<IssuedCoupon> getIssuedCouponsByIds(List<Long> issuedCouponIds) {
        return couponRepository.findAllByIssuedCouponIds(issuedCouponIds);
    }

    public Point calculateDiscountPoint(List<IssuedCoupon> issuedCouponIds) {
        return issuedCouponIds.stream()
                .map(IssuedCoupon::getDiscountAmount)
                .reduce(Point.ZERO, Point::add);
    }

    public void useCoupon(UseCouponCommand command) {
        User user = command.user();
        Payment payment = command.payment();
        command.issuedCoupons().forEach(
                item -> item.use(
                        user,
                        clockHolder,
                        payment.getId())

        );
    }

    public void issueCoupon(IssueCouponCommand command) {
        Coupon coupon = couponRepository.getById(
                command.couponId()
        );
        IssuedCoupon issuedCoupon = coupon.issue(
                command.user(), LocalDateTime.now()
        );
        couponRepository.saveIssuedCoupon(issuedCoupon);
    }

    public List<UserCouponsDetail> list(Long userId) {
        return couponRepository.findIssuedCouponsByUserId(userId)
                .stream()
                .map(UserCouponsDetail::from)
                .toList();
    }
}
