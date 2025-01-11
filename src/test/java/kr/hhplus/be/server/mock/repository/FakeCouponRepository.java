package kr.hhplus.be.server.mock.repository;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCouponRepository implements CouponRepository {

    private final AtomicLong autoIncrementedId = new AtomicLong(0);
    private final List<Coupon> couponData = Collections.synchronizedList(
        new ArrayList<>(
            List.of()
        )
    );
    private final List<IssuedCoupon> issuedCouponData = Collections.synchronizedList(
        new ArrayList<>()
    );

    @Override
    public Coupon getCoupon(Long couponId) {
        return null;
    }

    @Override
    public IssuedCoupon getIssuedCoupon(Long issuedCouponId) {
        return null;
    }

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
    public Coupon getById(Long couponId) {
        return couponData
                .stream()
                .filter(coupon -> coupon.getId().equals(couponId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "ID가 %d인 쿠폰이 존재하지 않습니다.".formatted(couponId)
                ));
    }

    @Override
    public void saveIssuedCoupon(IssuedCoupon issuedCoupon) {
        issuedCouponData.add(issuedCoupon);
    }

    public void saveAll(List<IssuedCoupon> issuedCoupons) {
        for (IssuedCoupon issuedCoupon : issuedCoupons) {
            save(issuedCoupon);
        }
    }

    public void save(IssuedCoupon issuedCouponFixture) {
        issuedCouponData.add(issuedCouponFixture);
    }

    public void save(Coupon couponFixture) {
        couponData.add(couponFixture);
    }

    public List<IssuedCoupon> getIssuedCoupons(Long userId) {
        return issuedCouponData
                .stream()
                .filter(issuedCoupon -> issuedCoupon.getUserId().equals(userId))
                .toList();
    }
}
