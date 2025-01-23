package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "COUPON_INVENTORY")
@Entity
public class CouponInventory extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Getter
    private Integer stock = 500;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    public CouponInventory(Coupon coupon, Integer stock) {
        this.coupon = coupon;
        this.stock = stock;
    }

    public CouponInventory(Coupon coupon) {
        this.coupon = coupon;
    }

    public void decreaseStock() {
        verifyCouponExists();
        this.stock--;
    }

    private void verifyCouponExists() {
        if (this.stock <= 0) {
            throw new BusinessException(
                    ErrorCode.COUPON_STOCK_EXHAUSTED,
                    "쿠폰 재고가 모두 소진되었습니다."
            );
        }
    }
}
