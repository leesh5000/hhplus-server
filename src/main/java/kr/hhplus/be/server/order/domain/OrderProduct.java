package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "ORDER_PRODUCT")
@Entity
public class OrderProduct extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private Integer quantity;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Point totalPrice;
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Order order;
    @Column(name = "product_id", nullable = false)
    private Long productId;

    public OrderProduct(Order order, Product product, Integer quantity) {
        this.order = order;
        this.productId = product.getId();
        this.quantity = quantity;
        this.totalPrice = product.getPrice().multiply(quantity);
    }
}
