package kr.hhplus.be.server.product.domain.service.dto.response;

import kr.hhplus.be.server.product.domain.Product;

import java.time.LocalDateTime;

public record ListProductsDetail(
        Long productId,
        String productName,
        Integer price,
        Integer stock,
        LocalDateTime createdAt
) {

    public static ListProductsDetail from(Product product) {
        return new ListProductsDetail(
                product.getId(),
                product.getName(),
                product.getPrice().toInt(),
                product.getStock(),
                product.getCreatedAt()
        );
    }
}
