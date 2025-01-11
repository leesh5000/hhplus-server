package kr.hhplus.be.server.user.interfaces;

import kr.hhplus.be.server.mock.TestContainer;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.interfaces.dto.request.UserPointRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@DisplayName("컨트롤러 단위 테스트 : 포인트 충전")
class ChargePointControllerTest {

    @Test
    @DisplayName("정상적인 값을 요청한 경우, 200 응답과 사용자 잔액 조회 API URL을 Location 헤더에 반환해야한다.")
    void chargeBalance_Success() {

        // Mocking
        TestContainer testContainer = new TestContainer();
        ChargePointController sut = testContainer
                .chargePointController;
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/api/v1/users/1/points");
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(mockRequest)
        );

        // given : ID가 1이고, 이름이 "항해99 유저"인 유저가 있을 때,
        User userFixture = new User(1L, "항해99 유저");
        testContainer.userRepository.save(userFixture);

        // When : 1000 포인트를 충전하면
        UserPointRequest request = new UserPointRequest(1000);
        ResponseEntity<Void> response = sut.charge(userFixture.getId(), request);

        // Then : 200 응답과 사용자 잔액 조회 API URL 을 Location 헤더에 반환해야한다.
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getHeaders().getFirst("Location")).isEqualTo("http://localhost/api/v1/users/1/points");

    }

}