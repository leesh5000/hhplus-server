package kr.hhplus.be.server.product.interfaces;

import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.mock.TestContainer;
import kr.hhplus.be.server.mock.domain.ProductFixture;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.interfaces.dto.response.ListProductsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("컨트롤러 단위 테스트 : 상품 목록 조회")
class ListProductsControllerTest {

    @DisplayName("현재 페이지와 페이지 크기를 전달하면," +
            "200 응답과 " +
            "페이지 정보 + 상품 목록을 응답한다.")
    @Test
    void listProducts() {

        // Mocking
        TestContainer testContainer = new TestContainer();

        Product productFixture1 = ProductFixture.create(1L);
        Product productFixture2 = ProductFixture.create(2L);
        Product productFixture3 = ProductFixture.create(3L);
        List<Product> products = new ArrayList<>(
                List.of(
                        productFixture1,
                        productFixture2,
                        productFixture3
                )
        );

        testContainer.productRepository.saveAll(products);

        ListProductsController sut = testContainer.listProductsController;

        // given
        int page = 0;
        int size = 20;

        // when
        ResponseEntity<PageDetails<List<ListProductsResponse>>> response = sut.listProducts(page, size);

        // then 1 : 200 응답
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // then 2 : 페이지 정보 + 상품 목록
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().page()).isEqualTo(0);
        assertThat(response.getBody().size()).isEqualTo(20);
        assertThat(response.getBody().totalElements()).isEqualTo(3);
        assertThat(response.getBody().totalPages()).isEqualTo(1);

        for (int i = 0; i < response.getBody().content().size(); i++) {
            assertThat(response.getBody().content().get(i).productId()).isEqualTo(products.get(i).getId().toString());
            assertThat(response.getBody().content().get(i).productName()).isEqualTo(products.get(i).getName());
            assertThat(response.getBody().content().get(i).price()).isEqualTo(products.get(i).getPrice().toLong().intValue());
            assertThat(response.getBody().content().get(i).stock()).isEqualTo(products.get(i).getStock());
        }

    }

}