package kr.hhplus.be.server.integration.user;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API 통합 테스트 : 포인트 충전")
@AutoConfigureMockMvc
@SpringBootTest
public class ChargePointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("""
           ID가 1인 유저가 존재할 때,
           
           1000 포인트를 충전 요청하면,
           
           {200 OK} 응답과
           Location 헤더에 {사용자 포인트 조회 URL}이 존재하고,
           ID가 1인 유저의 포인트가 {1000 만큼 충전} 되어야 한다.
           """)
    @Test
    void chargePoint() throws Exception {

        // given : ID가 1인 유저가 존재 (resources/sql/data.sql 참고)
        User user = userRepository.getById(1L);
        Point userPoint = user.getPoint();
        String userPointsUri = "http://localhost/api/v1/users/%s/points".formatted(user.getId());

        // when : ID가 1인 유저에 1000 포인트를 충전 요청하면
        mockMvc.perform(
                        post("/api/v1/users/{userId}/points", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "amount": 1000
                                }
                                """)
                )
                // then 1 : 200 OK 응답해야 한다.
                .andExpect(status().isOk())
                // then 2 : Location 헤더에 사용자 포인트 조회 URL 이 존재해야 한다.
                .andExpect(header().string("Location", userPointsUri));

        // then 3 : userID가 1인 유저의 포인트가 1000 만큼 충전되어야 한다.
        User updatedUser = userRepository.getById(1L);
        assertThat(updatedUser.getPoint()).isEqualTo(userPoint.add(1000));
    }

    @DisplayName("""
            ID가 1인 유저에
            
            최대 한도 포인트(2,100,000,001)를 충전 요청하면,
            
            {400 BAD_REQUEST 응답} 과
            errorCode 가 {INVALID_AMOUNT_REQUEST}이고
            message 가 {충전 요청 금액이 유효하지 않습니다.}인 에러 응답을 해야 한다.\
            """)
    @Test
    void chargePointFail() throws Exception {

        // given : ID가 1인 유저가 존재 (resources/sql/data.sql 참고)

        // when : 최대 한도 포인트(2,100,000,001)를 충전 요청하면
        mockMvc.perform(
                        post("/api/v1/users/1/points")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "amount": 2100000001
                                }
                                """)
                )
                // then 1 : 400 BAD_REQUEST 응답해야 한다.
                .andExpect(status().isBadRequest())
                // then 2 : errorCode 가 `INVALID_AMOUNT_REQUEST`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.errorCode").value("INVALID_AMOUNT_REQUEST"))
                // then 3 : message 가 `충전 요청 금액이 유효하지 않습니다.`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.message").value("충전 요청 금액이 유효하지 않습니다."))
        ;
    }

    @DisplayName("""
            최대 보유 한도 포인트를 가진 사용자가 존재할 때,
            
            포인트를 충전 요청하면,
            
            {409 CONFLICT} 응답과
            errorCode 가 {MAXIMUM_BALANCE_EXCEED}이고
            message 가 {"보유할 수 있는 최대 포인트 한도를 초과했습니다."}인 에러 응답을 해야 한다
            """)
    @SqlGroup({
            @Sql(scripts = "/sql/insert_max_balance_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/delete_max_balance_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void chargePointFail2() throws Exception {

        // given : 최대 보유 한도 포인트를 가진 사용자가 존재 (resources/sql/insert_max_balance_user.sql 참고)
        Long maxBalanceUserId = 100L;

        // when : 포인트를 충전 요청하면
        mockMvc.perform(
                        post("/api/v1/users/{userId}/points", maxBalanceUserId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "amount": 1000
                                }
                                """)
                )
                // then 1 : `409 CONFLICT 응답`해야 한다.
                .andExpect(status().isConflict())
                // then 2 : `errorCode 가 MAXIMUM_BALANCE_EXCEED`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.errorCode").value("MAXIMUM_BALANCE_EXCEED"))
                // then 3 : Message 가 `요청 바디가 없습니다.`인 에러 메세지를 응답해야 한다.
                .andExpect(jsonPath("$.message").value("보유할 수 있는 최대 포인트 한도를 초과했습니다."))
        ;
    }
}
