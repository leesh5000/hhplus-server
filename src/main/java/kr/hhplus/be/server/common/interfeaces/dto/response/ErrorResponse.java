package kr.hhplus.be.server.common.interfeaces.dto.response;

import kr.hhplus.be.server.common.domain.ErrorCode;

public record ErrorResponse(String errorCode, String message) {
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.name(), errorCode.getMessage());
    }
}
