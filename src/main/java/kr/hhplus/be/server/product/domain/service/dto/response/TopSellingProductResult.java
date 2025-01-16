package kr.hhplus.be.server.product.domain.service.dto.response;

import lombok.Getter;

@Getter
public class TopSellingProductResult {

    private final Long productId;
    private final String productName;
    private final Long price;
    private final Long salesCount;

    public TopSellingProductResult(Long productId, String productName, Long price, Long salesCount) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.salesCount = salesCount;
    }
}
