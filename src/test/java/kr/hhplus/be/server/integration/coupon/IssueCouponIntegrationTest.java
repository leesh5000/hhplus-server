package kr.hhplus.be.server.integration.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 통합 테스트 : 쿠폰 발급")
@SqlGroup({
        @Sql(value = "/sql/issue_coupon_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/delete_all_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
@AutoConfigureMockMvc
@SpringBootTest
public class IssueCouponIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("userId, couponId 를 입력하여 쿠폰 발급에 성공하면," +
            "200 응답과 Location 헤더에 `/api/v1/users/1/coupons`가 포함되어야 한다.")
    @Test
    void issue_coupon_test_1() throws Exception {

        // Mocking
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/api/v1/users/1/coupons/1");
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(mockRequest)
        );

        // given
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users/1/coupons/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                // Location 헤더에 "/api/v1/users/1/coupons"가 포함되어 있는지 확인
                .andExpect(header().string("Location", "http://localhost/api/v1/users/1/coupons/1"));
    }

}
