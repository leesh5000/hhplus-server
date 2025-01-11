package kr.hhplus.be.server.product.domain.service.dto.request;

public record FetchProductCommand(Long productId, Integer quantity) {
}
