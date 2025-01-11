package kr.hhplus.be.server.integration.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("API 통합 테스트 : 포인트 조회")
@SqlGroup({
        @Sql(value = "/sql/check_point_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/delete_all_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
@AutoConfigureMockMvc
@SpringBootTest
public class CheckPointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("userId를 입력하여 포인트 조회에 성공하면," +
            "200 응답과 " +
            "응답 본문에 `userId`와 `point`가 포함되어야 한다.")
    @Test
    void check_coupon_test() throws Exception {

        // Mocking
        // given
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/users/1/points")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                // 응답 본문에 `userId`와 `point`가 포함되어 있는지 확인
                .andExpect(jsonPath("$.point").value(0));
    }
}
