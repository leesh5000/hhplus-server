package kr.hhplus.be.server.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "product_inventory")
@Entity
public class ProductInventory extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    // 정책 : 기본 상품 재고는 100개이다.
    private Integer stock = 100;

    public ProductInventory(Integer stock) {
        this.stock = stock;
    }

    public void decrease(Integer quantity) {
        verifyStockSufficiency(quantity);
        stock -= quantity;
    }

    private void verifyStockSufficiency(Integer quantity) {
        if (stock < quantity) {
            throw new BusinessException(
                    ErrorCode.INSUFFICIENT_STOCK,
                    "재고가 부족합니다. 재고: %d, 주문 수량: %d".formatted(stock, quantity)
            );
        }
    }
}
