package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    Coupon getCoupon(Long couponId);
    IssuedCoupon getIssuedCoupon(Long issuedCouponId);
    List<IssuedCoupon> findIssuedCouponsByUserId(Long userId);
    Optional<IssuedCoupon> findIssuedCouponById(Long issuedCouponId);
    Coupon getById(Long couponId);
    void saveIssuedCoupon(IssuedCoupon issuedCoupon);
    List<IssuedCoupon> findAllByIssuedCouponIds(List<Long> issuedCouponIds);
}
