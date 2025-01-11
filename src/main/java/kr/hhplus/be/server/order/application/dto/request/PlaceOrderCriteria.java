package kr.hhplus.be.server.order.application.dto.request;

import kr.hhplus.be.server.order.domain.service.dto.request.OrderCommand;

import java.util.List;

public record PlaceOrderCriteria(
        Long userId,
        List<OrderProductCriteria> products,
        List<Long> issuedCouponIds
) {
    public record OrderProductCriteria(Long productId, Integer quantity) {
    }

    public OrderCommand toOrderCommand() {
        return new OrderCommand(
                userId,
                products.stream()
                        .map(product -> new OrderCommand.OrderProductCommand(
                                product.productId(),
                                product.quantity()
                        ))
                        .toList()
        );
    }
}
