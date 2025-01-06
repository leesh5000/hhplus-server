package kr.hhplus.be.server.user.interfaces.dto.request;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;

public record ChargeUserBalanceRequest(Integer amount) {
    public ChargeUserBalanceRequest {
        if (amount <= 0 || amount > 2100000000) {
            throw new BusinessException(
                    ErrorCode.INVALID_AMOUNT_REQUEST,
                    "사용자 충전 요청 금액 에러, 요청 금액: %d".formatted(amount)
            );
        }
    }
}
