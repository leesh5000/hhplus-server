package kr.hhplus.be.server.product.interfaces;

import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.mock.TestContainers;
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

    @DisplayName("""
            상품이 3개 존재하고,
            
            0번 페이지의 5개 사이즈로 상품 목록을 요청하면,
            
            {200 응답}과
            {현재 페이지가 0, 전체 페이지가 1, 총 상품 수가 3인 페이지 정보}와
            일치하는 {ID, 상품명, 가격, 재고 수}를 응답해야 한다.
            """)
    @Test
    void listProducts() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        ListProductsController sut = testContainers.listProductsController;

        // given : 상품이 3개 존재
        Product product1 = ProductFixture.create(1L);
        Product product2 = ProductFixture.create(2L);
        Product product3 = ProductFixture.create(3L);
        List<Product> products = new ArrayList<>(
                List.of(
                        product1,
                        product2,
                        product3
                )
        );
        testContainers.productRepository.saveAll(products);

        // when : 상품 목록을 요청하면
        ResponseEntity<PageDetails<List<ListProductsResponse>>> response = sut.listProducts(0, 5);

        // then 1 : 200 응답
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // then 2 : 현재 페이지가 0, 전체 페이지가 1, 총 상품 수가 3인 페이지 정보
        PageDetails<List<ListProductsResponse>> pageDetails = response.getBody();
        assertThat(pageDetails.page()).isEqualTo(0);
        assertThat(pageDetails.totalPages()).isEqualTo(1);
        assertThat(pageDetails.totalElements()).isEqualTo(3);

        List<ListProductsResponse> listProductsResponses = pageDetails.content();
        for (int i = 0; i < listProductsResponses.size(); i++) {
            ListProductsResponse listProductsResponse = listProductsResponses.get(i);
            Product product = products.get(i);
            // then 3 : 일치하는 ID, 상품명, 가격, 재고 수를 응답해야 한다.
            assertThat(listProductsResponse.productId()).isEqualTo(product.getId().toString());
            assertThat(listProductsResponse.productName()).isEqualTo(product.getName());
            assertThat(listProductsResponse.price()).isEqualTo(product.getPrice().toInt());
            assertThat(listProductsResponse.stock()).isEqualTo(product.getStock());
        }

    }

}