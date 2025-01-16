package kr.hhplus.be.server.common.interfeaces;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.interfeaces.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        ErrorCode errorCode = e.getErrorCode();
        return buildErrorResponse(errorCode);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException", e);
        BusinessException businessException = extractBusinessException(e);
        return businessException != null ?
                buildErrorResponse(businessException.getErrorCode()) :
                buildErrorResponse(ErrorCode.REQUEST_BODY_MISSING);
    }

    private static BusinessException extractBusinessException(Throwable e) {
        while (e.getCause() != null) {
            if (e.getCause() instanceof BusinessException) {
                return (BusinessException) e.getCause();
            }
            e = e.getCause();
        }
        return null;
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode) {
        int statusCode = errorCode.getStatusCode();
        ErrorResponse response = errorCode.toErrorResponse();
        return ResponseEntity
                .status(statusCode)
                .body(response);
    }
}