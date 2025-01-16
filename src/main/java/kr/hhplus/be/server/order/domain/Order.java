package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "ORDERS")
@Entity
public class Order extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "order_price"))
    private Point orderPrice = Point.ZERO;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(User orderer) {
        this.userId = orderer.getId();
    }

    public void addProduct(Product product, Integer quantity) {
        verifyExceedMaximumOrderProducts();
        OrderProduct orderProduct = new OrderProduct(this, product, quantity);
        orderProducts.add(orderProduct);
        orderPrice = orderPrice.add(orderProduct.getTotalPrice());
    }

    private void verifyExceedMaximumOrderProducts() {
        if (orderProducts.size() > 10) {
            throw new BusinessException(
                    ErrorCode.EXCEED_MAXIMUM_ORDER_PRODUCT,
                    "한 번에 주문할 수 있는 상품은 최대 10개 까지만 가능합니다."
            );
        }
    }
}
