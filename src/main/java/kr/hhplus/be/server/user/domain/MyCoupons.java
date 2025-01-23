package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@Embeddable
public class MyCoupons {

    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "ISSUED_COUPON",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private List<IssuedCoupon> coupons = new ArrayList<>();

    public MyCoupons(List<IssuedCoupon> coupons) {
        this.coupons = coupons;
    }

    public void add(IssuedCoupon issuedCoupon) {
        coupons.add(issuedCoupon);
    }

    public List<IssuedCoupon> list() {
        return coupons;
    }

    public List<IssuedCoupon> list(Long couponId) {
        return coupons.stream()
                .filter(issuedCoupon -> issuedCoupon
                        .getCouponId()
                        .equals(couponId))
                .toList();
    }

    public Integer size() {
        return coupons.size();
    }

}
