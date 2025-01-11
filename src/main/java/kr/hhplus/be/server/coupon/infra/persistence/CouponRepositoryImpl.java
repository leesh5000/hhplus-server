package kr.hhplus.be.server.coupon.infra.persistence;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.infra.persistence.jpa.CouponJpaRepository;
import kr.hhplus.be.server.coupon.infra.persistence.jpa.IssuedCouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

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
        return issuedCouponJpaRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<IssuedCoupon> findIssuedCouponById(Long issuedCouponId) {
        return issuedCouponJpaRepository.findById(issuedCouponId);
    }

    @Override
    public Coupon getById(Long couponId) {
        return couponJpaRepository.findById(couponId).orElseThrow();
    }

    @Override
    public void saveIssuedCoupon(IssuedCoupon issuedCoupon) {
        issuedCouponJpaRepository.save(issuedCoupon);
    }
}
