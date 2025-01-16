package kr.hhplus.be.server.common.domain;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String logMessage;

    public BusinessException(ErrorCode errorCode, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
        this.logMessage = logMessage;
    }
}
