package kr.hhplus.be.server.user.interfaces;

import kr.hhplus.be.server.mock.TestContainers;
import kr.hhplus.be.server.mock.domain.UserFixture;
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
    @DisplayName("""
            ID가 1인 사용자가 존재할 때,
            
            1000 포인트를 충전하면,
            
            {200 응답}과
            {사용자 잔액 조회 API URL} 을 Location 헤더에 반환해야 한다.
            """)
    void chargeBalance_Success() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        ChargePointController sut = testContainers.chargePointController;
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/api/v1/users/1/points");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // given : ID가 1인 사용자가 존재
        User user = UserFixture.create(1L);
        testContainers.userRepository.save(user);

        // when : 1000 포인트를 충전하면
        UserPointRequest request = new UserPointRequest(1000);
        ResponseEntity<Void> response = sut.charge(1L, request);

        // then 1 : 200 응답
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        // then 2 : 사용자 잔액 조회 API URL 을 Location 헤더에 반환해야 한다.
        Assertions.assertThat(response.getHeaders().getFirst("Location")).isEqualTo("http://localhost/api/v1/users/1/points");

    }

}