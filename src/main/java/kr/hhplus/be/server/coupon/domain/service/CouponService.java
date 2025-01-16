package kr.hhplus.be.server.coupon.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.common.domain.service.DateTimeHolder;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.domain.service.dto.response.UserCouponsDetail;
import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final DateTimeHolder dateTimeHolder;

    public List<IssuedCoupon> getIssuedCouponsByIds(List<Long> issuedCouponIds) {
        return couponRepository.findAllByIssuedCouponIds(issuedCouponIds);
    }

    public IssuedCoupon getIssuedCouponById(Long issuedCouponId) {
        return couponRepository.findIssuedCouponById(issuedCouponId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "ID가 %s인 발급된 쿠폰이 존재하지 않습니다.".formatted(issuedCouponId)
                ));
    }

    public Point useCoupon(User couponOwner, List<Long> issuedCouponIds) {
        List<IssuedCoupon> issuedCoupons = this.getIssuedCouponsByIds(issuedCouponIds);
        return issuedCoupons.stream()
                .map(issuedCoupon -> issuedCoupon.use(couponOwner, dateTimeHolder))
                .reduce(Point.ZERO, Point::add);
    }

    public List<UserCouponsDetail> list(Long userId) {
        return couponRepository.findIssuedCouponsByUserId(userId)
                .stream()
                .map(UserCouponsDetail::from)
                .toList();
    }

    public Coupon getById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "ID가 %s인 쿠폰이 존재하지 않습니다.".formatted(couponId)
                ));
    }

    public void issue(Long couponId, User user) {
        Coupon coupon = getById(couponId);
        IssuedCoupon issuedCoupon = coupon.issue(user,
                dateTimeHolder.now());
        couponRepository.saveIssuedCoupon(issuedCoupon);
    }
}
