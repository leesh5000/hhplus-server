package kr.hhplus.be.server.order.interfaces.dto;

import kr.hhplus.be.server.order.application.dto.request.PlaceOrderCriteria;

import java.util.List;

public record PlaceOrderRequest(
        Long userId,
        List<OrderProductRequest> products,
        List<Long> issuedCouponIds
) {

    public record OrderProductRequest(
            Long productId,
            Integer quantity
    ) {
    }

    public PlaceOrderCriteria toCriteria() {
        return new PlaceOrderCriteria(
                userId,
                products.stream()
                        .map(product -> new PlaceOrderCriteria.OrderProductCriteria(
                                product.productId(),
                                product.quantity()
                        ))
                        .toList(),
                issuedCouponIds
        );
    }
}
