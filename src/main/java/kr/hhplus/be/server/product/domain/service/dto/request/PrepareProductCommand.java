package kr.hhplus.be.server.product.domain.service.dto.request;

public record PrepareProductCommand(
        Long productId,
        Integer quantity) {
}
