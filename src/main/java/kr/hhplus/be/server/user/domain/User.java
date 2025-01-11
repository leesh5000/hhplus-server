package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "USER")
@Entity
public class User extends BaseEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String name;
    @Getter
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "id", referencedColumnName = "user_id", nullable = false)
    private Wallet wallet = new Wallet();
    @OneToMany(fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssuedCoupon> coupons = new ArrayList<>();

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

    public boolean isSaved() {
        return id != null;
    }

    public WalletHistory deductPoint(Point deductPoint) {
        return wallet.withdraw(deductPoint.toInt());
    }
}
