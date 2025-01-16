package kr.hhplus.be.server.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Embeddable
public class MyCoupons {
    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssuedCoupon> coupons = new ArrayList<>();

    public void add(IssuedCoupon issuedCoupon) {
        coupons.add(issuedCoupon);
    }

    public List<IssuedCoupon> list() {
        return coupons;
    }
}
