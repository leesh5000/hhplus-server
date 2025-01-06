package kr.hhplus.be.server.common.domain;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_AMOUNT_REQUEST("충전 요청 금액이 유효하지 않습니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
