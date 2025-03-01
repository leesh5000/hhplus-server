package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.common.domain.service.DateTimeHolder;
import kr.hhplus.be.server.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "ISSUED_COUPON")
@Entity
public class IssuedCoupon extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private Boolean used = Boolean.FALSE;
    @Column(name = "used_at")
    private LocalDateTime usedAt = null;
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Coupon coupon;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public IssuedCoupon(Long userId, Coupon coupon) {
        this(null, userId, coupon);
    }

    public IssuedCoupon(Long id, Long userId, Coupon coupon) {
        this.id = id;
        this.userId = userId;
        this.coupon = coupon;
    }

    public Long getCouponId() {
        return coupon.getId();
    }

    public Boolean isUsed() {
        return used;
    }

    public String getCouponName() {
        return coupon.getName();
    }

    public LocalDateTime getExpiredAt() {
        return coupon.getExpiredAt();
    }

    public Point getDiscountAmount() {
        return coupon.getDiscountAmount();
    }

    public LocalDateTime getIssuedAt() {
        return getCreatedAt();
    }

    public Point use(
            User user,
            DateTimeHolder dateTimeHolder) {

        verifyCouponOwner(user);
        verifyCouponNotExpire(dateTimeHolder);
        verifyCouponNotUsed();
        this.used = Boolean.TRUE;
        this.usedAt = dateTimeHolder.now();

        return this.getDiscountAmount();
    }

    private void verifyCouponNotExpire(DateTimeHolder dateTimeHolder) {
        LocalDateTime now = dateTimeHolder.now();
        if (coupon.isExpire(now)) {
            throw new BusinessException(
                    ErrorCode.EXPIRED_COUPON,
                    "쿠폰이 만료되었습니다.");
        }
    }

    private void verifyCouponOwner(User user) {
        if (user.getId().equals(userId)) {
            throw new RuntimeException("잘못된 요청입니다.");
        }
    }

    private void verifyCouponNotUsed() {
        if (used) {
            throw new BusinessException(
                    ErrorCode.ALREADY_USED_COUPON,
                    "이미 사용한 쿠폰입니다.");
        }
    }
}
