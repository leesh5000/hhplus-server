package kr.hhplus.be.server.user.interfaces;

import kr.hhplus.be.server.mock.TestContainers;
import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.interfaces.dto.response.CheckPointResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

@DisplayName("컨트롤러 단위 테스트 : 포인트 조회")
class CheckPointControllerTest {

    @Test
    @DisplayName("""
            ID가 1이고, 2000 포인트를 가진 유저가 존재할 때,
            
            포인트를 조회하면,
            
            {200 응답}과
            응답 본문에 {2000 포인트}를 반환해야 한다.
            """)
    void getBalance_Success() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        CheckPointController sut = testContainers.checkPointController;

        // given : ID가 1이고, 2000 포인트를 가진 유저가 존재
        User user = UserFixture.create(1L, 2000L);
        testContainers.userRepository.save(user);

        // When : 포인트를 조회하면
        ResponseEntity<CheckPointResponse> response = sut.check(1L);

        // Then 1 : 200 응답
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        // Then 2 : 응답 본문에 2000 포인트를 반환해야 한다.
        Assertions.assertThat(response.getBody().point()).isEqualTo(2000);

    }

}