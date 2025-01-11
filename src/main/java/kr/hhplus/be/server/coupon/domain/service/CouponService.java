package kr.hhplus.be.server.coupon.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.common.domain.service.ClockHolder;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.service.dto.response.UserCouponsDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final ClockHolder clockHolder;

    public Point calculateDiscountPoint(List<Long> issuedCouponIds) {
        return issuedCouponIds.stream()
                .map(couponRepository::findIssuedCouponById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(IssuedCoupon::getDiscountAmount)
                .reduce(Point.ZERO, Point::add);
    }

    public void useCoupons(List<Long> issuedCouponIds, Long paymentId) {
        issuedCouponIds
                .forEach(issuedCouponId ->
                        useCoupon(issuedCouponId, paymentId));

    }

    public void useCoupon(Long issuedCouponId, Long paymentId) {
        IssuedCoupon issuedCoupon = couponRepository.findIssuedCouponById(issuedCouponId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "ID가 %d인 발급된 쿠폰이 존재하지 않습니다.".formatted(issuedCouponId)
                        ));
        issuedCoupon.use(clockHolder, paymentId);
    }

    public void issueCoupon(Long couponId, Long userId) {
        Coupon coupon = couponRepository.getById(couponId);
        IssuedCoupon issuedCoupon = coupon.issue(userId);
        couponRepository.saveIssuedCoupon(issuedCoupon);
    }

    public List<UserCouponsDetail> list(Long userId) {
        return couponRepository.findIssuedCouponsByUserId(userId)
                .stream()
                .map(UserCouponsDetail::from)
                .toList();
    }

}
