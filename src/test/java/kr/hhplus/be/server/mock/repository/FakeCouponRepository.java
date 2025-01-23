package kr.hhplus.be.server.mock.repository;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeCouponRepository implements CouponRepository {

    private final List<Coupon> couponData = Collections.synchronizedList(
        new ArrayList<>(
            List.of()
        )
    );
    private final List<IssuedCoupon> issuedCouponData = Collections.synchronizedList(
        new ArrayList<>()
    );

    @Override
    public List<IssuedCoupon> findIssuedCouponsByUserId(Long userId) {
        return issuedCouponData
                .stream()
                .filter(issuedCoupon -> issuedCoupon.getUserId().equals(userId))
                .toList();
    }

    @Override
    public Optional<IssuedCoupon> findIssuedCouponById(Long issuedCouponId) {
        return Optional.empty();
    }

    @Override
    public Optional<Coupon> findByIdWithPessimisticLock(Long couponId) {
        return couponData
                .stream()
                .filter(coupon -> coupon.getId().equals(couponId))
                .findFirst();
    }

    @Override
    public void save(IssuedCoupon issuedCoupon) {
        issuedCouponData.add(issuedCoupon);
    }

    @Override
    public List<IssuedCoupon> findAllByIssuedCouponIds(List<Long> issuedCouponIds) {
        return issuedCouponData
                .stream()
                .filter(issuedCoupon -> issuedCouponIds.contains(issuedCoupon.getId()))
                .toList();
    }

    public void saveAll(List<IssuedCoupon> issuedCoupons) {
        for (IssuedCoupon issuedCoupon : issuedCoupons) {
            save(issuedCoupon);
        }
    }

    public void save(Coupon coupon) {
        couponData.add(coupon);
    }

    @Override
    public void deleteAll() {
        couponData.clear();
        issuedCouponData.clear();
    }

    public Long countIssuedCouponsByUserId(Long userId) {
        return issuedCouponData
                .stream()
                .filter(issuedCoupon -> issuedCoupon.getUserId().equals(userId))
                .count();
    }
}
