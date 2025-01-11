package kr.hhplus.be.server.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "order_product")
@Entity
public class OrderProduct extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private Integer quantity;
    private Point totalPrice;
    private Long orderId;
    private Long productId;

    public OrderProduct(Integer quantity, Product product) {
        this.quantity = quantity;
        this.totalPrice = product.getPrice().multiply(quantity);
        this.productId = product.getId();
    }
}
