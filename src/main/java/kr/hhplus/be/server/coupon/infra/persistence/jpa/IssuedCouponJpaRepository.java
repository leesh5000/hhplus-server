package kr.hhplus.be.server.coupon.infra.persistence.jpa;

import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {
    List<IssuedCoupon> findAllByUserId(Long userId);
}
