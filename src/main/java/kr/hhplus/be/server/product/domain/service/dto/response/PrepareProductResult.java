package kr.hhplus.be.server.product.domain.service.dto.response;

import kr.hhplus.be.server.product.domain.Product;

public record PrepareProductResult(
        Product product,
        Integer quantity) {
}
