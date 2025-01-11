package kr.hhplus.be.server.user.interfaces;

import kr.hhplus.be.server.mock.TestContainer;
import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.user.interfaces.dto.response.CheckPointResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

@DisplayName("컨트롤러 단위 테스트 : 포인트 조회")
class CheckPointControllerTest {

    @Test
    @DisplayName("ID가 1이고, 2000 포인트를 가진 유저가 존재할 때," +
            "포인트 조회 API를 호출하면, " +
            "200 응답과 함께 2000 포인트를 반환해야 한다.")
    void getBalance_Success() {

        // Mocking
        TestContainer testContainer = new TestContainer();
        testContainer.userRepository.save(
                UserFixture.create(1L, 2000L)
        );
        CheckPointController sut = testContainer.checkPointController;

        // Given
        Long userId = 1L;

        // When
        ResponseEntity<CheckPointResponse> response = sut.check(userId);

        // Then
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().point()).isEqualTo(2000);

    }

}