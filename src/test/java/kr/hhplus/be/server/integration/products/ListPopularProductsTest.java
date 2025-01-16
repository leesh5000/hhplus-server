package kr.hhplus.be.server.integration.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 통합 테스트 : 인기 상품 조회")
@AutoConfigureMockMvc
@SpringBootTest
class ListPopularProductsTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("""
            인기 상품 목록이 존재하고,
            
            인기 상품 조회 API 를 호출하면,
            
            {200 OK} 응답과
            인기 상품 목록이 반환되어야 한다.
            
            인기 상품 목록 정보는
            {상품 ID, 상품명, 판매 수량}이어야 한다.
            """)
    @SqlGroup({
            @Sql(value = "/sql/insert_top_sold_product.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete_top_sold_product.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void getListPopularProducts() throws Exception {

         // given : (insert_top_sold_product.sql 참고)

         // when : 인기 상품 조회 API 호출
         mockMvc.perform(
                            get("/products/top-sales")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("day", "3")
                                    .param("limit", "5")
                    )
                    // then : 200 OK 응답해야 함
                    .andExpect(status().isOk())

                    // then : 인기 상품 목록이 반환되어야 함
                    .andExpect(jsonPath("$").isArray())
                    // then : 인기 상품 목록 정보는 {상품 ID, 상품명, 판매 수량}이어야 함
                    .andExpect(jsonPath("$.[0].productId").isNumber())
                    .andExpect(jsonPath("$.[0].productName").isString())
                    .andExpect(jsonPath("$.[0].salesCount").isNumber())
         ;
    }

}