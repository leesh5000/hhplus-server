package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    private Point orderPrice;
    private Long userId;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(Long userId, List<OrderProduct> orderProducts) {
        this.userId = userId;
        this.orderProducts.addAll(orderProducts);
        this.orderPrice = calculateOrderPrice();
    }

    private Point calculateOrderPrice() {
        return orderProducts.stream()
                .map(OrderProduct::getTotalPrice)
                .reduce(Point.ZERO, Point::add);
    }

    private void addOrderProduct(Product product, Integer quantity) {
        OrderProduct orderProduct = new OrderProduct(quantity, product);
        orderProducts.add(orderProduct);
    }
}
