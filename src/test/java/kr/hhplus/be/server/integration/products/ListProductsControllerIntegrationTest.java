package kr.hhplus.be.server.integration.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 통합 테스트 : 상품 목록 조회")
@AutoConfigureMockMvc
@SpringBootTest
public class ListProductsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("""
            20개의 상품이 존재할 때,
            
            0번째 페이지로 10개의 상품을 조회하면,
            
            {200 OK} 응답과
            
            응답 본문에는
            
            {page}가 {0} 이고,
            {size}가 {10} 이고,
            {totalElements}가 {20} 이고,
            {totalPages}가 {2} 이고,
            
            {content}에는 {10개의 상품}이 반환되어야 하고,
            
            각 상품 정보는
            
            {productId}가 {1부터 10까지}이고,
            {productName}이 {product1부터 product10까지}이고,
            {price}가 {10000부터 100000까지}이어야 한다.
            """)
    @Test
    void listProducts() throws Exception {

        // given : 20개의 상품이 존재 (resources/sql/data.sql 참고)

        // when : 상품 목록을 조회하면
        mockMvc.perform(
                        get("/api/v1/products")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                // then 1 : 200 OK 응답해야 한다.
                .andExpect(status().isOk())

                // then 2 : 응답 본문에는
                // {page}가 {0} 이고,
                .andExpect(jsonPath("$.page").value(0))
                // {size}가 {10} 이고,
                .andExpect(jsonPath("$.size").value(10))
                // {totalElements}가 {20} 이고,
                .andExpect(jsonPath("$.totalElements").value(20))
                // {totalPages}가 {2} 이고,
                .andExpect(jsonPath("$.totalPages").value(2))
                // {content}에는 {10개의 상품}이 반환되어야 한다.
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content.length()").value(10))

                // then 3 : 각 상품 정보는 {productId}가 {1부터 10까지}이고,
                .andExpect(jsonPath("$.content[0].productId").value(1))
                .andExpect(jsonPath("$.content[1].productId").value(2))
                .andExpect(jsonPath("$.content[2].productId").value(3))
                .andExpect(jsonPath("$.content[3].productId").value(4))
                .andExpect(jsonPath("$.content[4].productId").value(5))
                .andExpect(jsonPath("$.content[5].productId").value(6))
                .andExpect(jsonPath("$.content[6].productId").value(7))
                .andExpect(jsonPath("$.content[7].productId").value(8))
                .andExpect(jsonPath("$.content[8].productId").value(9))
                .andExpect(jsonPath("$.content[9].productId").value(10))

                // {productName}이 {product1부터 product10까지}이고,
                .andExpect(jsonPath("$.content[0].productName").value("product1"))
                .andExpect(jsonPath("$.content[1].productName").value("product2"))
                .andExpect(jsonPath("$.content[2].productName").value("product3"))
                .andExpect(jsonPath("$.content[3].productName").value("product4"))
                .andExpect(jsonPath("$.content[4].productName").value("product5"))
                .andExpect(jsonPath("$.content[5].productName").value("product6"))
                .andExpect(jsonPath("$.content[6].productName").value("product7"))
                .andExpect(jsonPath("$.content[7].productName").value("product8"))
                .andExpect(jsonPath("$.content[8].productName").value("product9"))
                .andExpect(jsonPath("$.content[9].productName").value("product10"))

                // {productPrice}가 {10000부터 100000까지}이어야 한다.
                .andExpect(jsonPath("$.content[0].price").value(10000))
                .andExpect(jsonPath("$.content[1].price").value(20000))
                .andExpect(jsonPath("$.content[2].price").value(30000))
                .andExpect(jsonPath("$.content[3].price").value(40000))
                .andExpect(jsonPath("$.content[4].price").value(50000))
                .andExpect(jsonPath("$.content[5].price").value(60000))
                .andExpect(jsonPath("$.content[6].price").value(70000))
                .andExpect(jsonPath("$.content[7].price").value(80000))
                .andExpect(jsonPath("$.content[8].price").value(90000))
                .andExpect(jsonPath("$.content[9].price").value(100000))
                ;
    }
}
