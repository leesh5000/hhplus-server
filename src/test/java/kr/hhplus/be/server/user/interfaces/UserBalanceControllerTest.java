package kr.hhplus.be.server.user.interfaces;

import kr.hhplus.be.server.user.interfaces.dto.request.ChargeUserBalanceRequest;
import kr.hhplus.be.server.user.interfaces.dto.response.UserBalanceResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@WebMvcTest(UserBalanceController.class)
class UserBalanceControllerTest {

    @Test
    @DisplayName("정상적인 값을 요청한 경우, 200 응답과 사용자 잔액 조회 API URL을 Location 헤더에 반환해야한다.")
    void chargeBalance_Success() {

        // Given
        Long userId = 1L;
        ChargeUserBalanceRequest request = new ChargeUserBalanceRequest(1000);
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/api/v1/users/1/balances");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        UserBalanceController sut = new UserBalanceController();

        // When
        ResponseEntity<Void> response = sut.charge(userId, request);

        // Then
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getHeaders().getFirst("Location")).isEqualTo("http://localhost/api/v1/users/1/balances");

    }

    @Test
    @DisplayName("정상적인 값을 요청한 경우, 200 응답과 사용자 잔액 정보를 반환해야한다.")
    void getBalance_Success() {

        // Given
        Long userId = 1L;
        UserBalanceController sut = new UserBalanceController();

        // When
        ResponseEntity<UserBalanceResponse> response = sut.get(userId);

        // Then
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().userId()).isEqualTo(userId.toString());
        Assertions.assertThat(response.getBody().balance()).isEqualTo(1000);
    }

}