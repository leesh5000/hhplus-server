package kr.hhplus.be.server.common.domain;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode code;
    private final String logMessage;

    public BusinessException(ErrorCode code, String logMessage) {
        super(logMessage);
        this.code = code;
        this.logMessage = logMessage;
    }
}
