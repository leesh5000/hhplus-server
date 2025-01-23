package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.product.domain.Product;

public class ProductFixture {

    private ProductFixture() {
    }

    public static Product create(Long id) {
        return new Product(
                id,
                "상품" + id,
                1000,
                100
        );
    }

    public static Product create(Long id, int price) {
        return new Product(
                id,
                "상품" + id,
                price,
                100
        );
    }
}
