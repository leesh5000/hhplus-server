package kr.hhplus.be.server.order.application.dto.request;

import kr.hhplus.be.server.order.domain.service.dto.request.PlaceOrderCommand;
import kr.hhplus.be.server.user.domain.User;

import java.util.List;

public record PlaceOrderCriteria(
        Long userId,
        List<OrderProductCriteria> products,
        List<Long> issuedCouponIds
) {
    public record OrderProductCriteria(Long productId, Integer quantity) {
    }

    public PlaceOrderCommand toPlaceOrderCommand(User orderer) {
        return new PlaceOrderCommand(
                orderer,
                products.stream()
                        .map(product -> new PlaceOrderCommand.OrderProduct(
                                product.productId(),
                                product.quantity()
                        ))
                        .toList()
        );
    }
}
