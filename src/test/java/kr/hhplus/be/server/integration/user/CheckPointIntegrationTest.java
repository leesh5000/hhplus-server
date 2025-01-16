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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("API 통합 테스트 : 포인트 조회")
@AutoConfigureMockMvc
@SpringBootTest
public class CheckPointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("""
            ID가 1이고, 보유 포인트가 10000인 유저가 존재할 때,
            
            포인트 조회 요청을 하면,
            
            {200 OK} 응답과
            응답 본문에 {point 필드가 10000} 이어야 한다.
            """
    )
    @SqlGroup({
            @Sql(scripts = "/sql/insert_10000_balance_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/delete_10000_balance_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void check_coupon_test() throws Exception {

        // given : ID가 1이고, 보유 포인트가 10000인 유저가 존재 (resources/sql/data.sql 참고)
        Long id = 100L;

        // when : 포인트 조회 요청을 하면
        mockMvc.perform(
                        get("/api/v1/users/{userId}/points", id)
                                .contentType(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                )
                // then 1 : 200 OK 응답해야 한다.
                .andExpect(status().isOk())
                // then 2 : 응답 본문에 point 필드가 10000 이어야 한다.
                .andExpect(jsonPath("$.point").value(10000));
    }
}
