package kr.hhplus.be.server.order.interfaces.dto;

import kr.hhplus.be.server.order.application.dto.request.OrderAndPayCriteria;

import java.util.List;

public record PlaceOrderRequest(
        Long userId,
        List<OrderProductRequest> products,
        List<Long> applyCouponIds
) {

    public record OrderProductRequest(
            Long productId,
            Integer quantity
    ) {
    }

    public OrderAndPayCriteria toCriteria() {
        return new OrderAndPayCriteria(
                userId,
                products.stream()
                        .map(product -> new OrderAndPayCriteria.OrderProduct(
                                product.productId(),
                                product.quantity()
                        ))
                        .toList(),
                applyCouponIds
        );
    }
}
