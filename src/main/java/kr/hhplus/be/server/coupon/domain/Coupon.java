package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.FetchType.EAGER;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "COUPON")
@Entity
public class Coupon extends BaseEntity {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Getter
    private String name;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "discount_amount"))
    @Getter
    private Point discountAmount;
    @OneToOne(fetch = EAGER, cascade = CascadeType.ALL, optional = false, mappedBy = "coupon", orphanRemoval = true)
    private CouponInventory inventory = new CouponInventory(this);
    @Getter
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public Coupon(Long id,
                  String name,
                  Long discountAmount,
                  Integer stock,
                  LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.discountAmount = new Point(discountAmount);
        this.inventory = new CouponInventory(this, stock);
        this.expiredAt = expiredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public IssuedCoupon issue(User user, LocalDateTime issueDate) {
        verifyCouponNotExpired(issueDate);
        this.inventory.decreaseStock();
        return new IssuedCoupon(
                user.getId(),
                this);
    }

    private void verifyCouponNotExpired(LocalDateTime issueDate) {
        if (isExpire(issueDate)) {
            throw new BusinessException(ErrorCode.EXPIRED_COUPON, "쿠폰이 만료되었습니다.");
        }
    }

    public boolean isExpire(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public Integer getStock() {
        return inventory.getStock();
    }
}
