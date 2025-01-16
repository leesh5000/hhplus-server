package kr.hhplus.be.server.order.domain.service.dto.request;

import kr.hhplus.be.server.product.domain.Product;

public record PlaceOrderCommand(
        Product product,
        Integer quantity) {
}
