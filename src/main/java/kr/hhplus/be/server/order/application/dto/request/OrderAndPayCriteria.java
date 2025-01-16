package kr.hhplus.be.server.order.application.dto.request;

import kr.hhplus.be.server.product.domain.service.dto.request.PrepareProductCommand;

import java.util.List;

public record OrderAndPayCriteria(
        Long userId,
        List<OrderProduct> products,
        List<Long> applyCouponIds) {

    public List<PrepareProductCommand> toPrepareProductCommands() {
        return products.stream()
                .map(product -> new PrepareProductCommand(
                        product.productId(),
                        product.quantity()
                ))
                .toList();
    }

    public record OrderProduct(
            Long productId,
            Integer quantity) {
    }
}
