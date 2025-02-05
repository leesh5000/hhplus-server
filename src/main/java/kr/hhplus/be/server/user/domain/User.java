package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "USER")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(fetch = EAGER, cascade = CascadeType.ALL, optional = false, mappedBy = "user", orphanRemoval = true)
    private Wallet wallet = new Wallet(this);
    @Embedded
    private final MyCoupons myCoupons = new MyCoupons();

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Long id, String name, Long initialPoint) {
        this.id = id;
        this.name = name;
        this.wallet = new Wallet(this, initialPoint);
    }

    public WalletHistory chargePoint(Integer amount) {
        return wallet.deposit(amount);
    }

    public WalletHistory deductPoint(Integer amount) {
        return wallet.withdraw(amount);
    }

    public Point getPoint() {
        return wallet.getBalance();
    }

    public WalletHistory deductPoint(Point deductPoint) {
        return wallet.withdraw(deductPoint.toInt());
    }

    public MyCoupons getMyCoupons(Long couponId) {
        List<IssuedCoupon> issuedCouponsByCouponId = myCoupons.list(couponId);
        return new MyCoupons(issuedCouponsByCouponId);
    }

}
