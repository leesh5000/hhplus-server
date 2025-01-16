package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @OneToOne(fetch = EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "id", referencedColumnName = "user_id", nullable = false)
    private Wallet wallet = new Wallet();
    @Embedded
    private final MyCoupons myCoupons = new MyCoupons();

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Long id, String name, Long point) {
        this.id = id;
        this.name = name;
        wallet.deposit(point);
    }

    public User(Long id, String name, Wallet wallet) {
        this.id = id;
        this.name = name;
        this.wallet = wallet;
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

}
