package kr.hhplus.be.server.product.interfaces.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.product.domain.service.dto.response.ListProductsDetail;

import java.time.LocalDateTime;

public record ListProductsResponse(
        String productId,
        String productName,
        Integer price,
        Integer stock,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
        LocalDateTime createdAt
) {
    public static ListProductsResponse from(ListProductsDetail result) {
        return new ListProductsResponse(
                result.productId().toString(),
                result.productName(),
                result.price(),
                result.stock(),
                result.createdAt()
        );
    }
}
