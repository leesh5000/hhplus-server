package kr.hhplus.be.server.user.interfaces.dto.request;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("사용자 포인트 충전 요청 DTO 단위 테스트")
class UserPointRequestTest {

    @Test
    @DisplayName("정상적인 값을 요청한 경우, 객체 생성이 성공해야 한다.")
    void chargeUserBalanceRequest_Success() {
        // Given
        Integer amount = 1000;

        // When & Then
        UserPointRequest request = new UserPointRequest(amount);
        assertEquals(amount, request.amount());
    }

    @Test
    @DisplayName("0보다 작은 값을 요청한 경우, `INVALID_AMOUNT_REQUEST` 에러 코드를 가진 `BusinessException`이 발생해야한다.")
    void chargeUserBalanceRequest_InvalidAmount() {
        // Given
        Integer amount = -1000;

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> new UserPointRequest(amount));
        assertEquals(ErrorCode.INVALID_AMOUNT_REQUEST, exception.getCode());
    }

    @Test
    @DisplayName("2100000000보다 큰 값을 요청한 경우, `INVALID_AMOUNT_REQUEST` 에러 코드를 가진 `BusinessException`이 발생해야한다.")
    void chargeUserBalanceRequest_InvalidAmount2() {
        // Given
        Integer amount = 2100000001;

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> new UserPointRequest(amount));
        assertEquals(ErrorCode.INVALID_AMOUNT_REQUEST, exception.getCode());
    }
}