package kr.hhplus.be.server.common.domain;

import kr.hhplus.be.server.common.interfeaces.dto.response.ErrorResponse;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    // 400
    REQUEST_BODY_MISSING("요청 바디가 없습니다."),
    INVALID_AMOUNT_REQUEST("충전 요청 금액이 유효하지 않습니다."),
    EXPIRED_COUPON("이미 만료일이 지난 쿠폰입니다."),
    ALREADY_USED_COUPON("이미 사용한 쿠폰입니다."),
    // 404
    RESOURCE_NOT_FOUND(NOT_FOUND.value(), "해당 리소스를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(NOT_FOUND.value(), "판매 종료된 상품입니다."),
    // 409
    MAXIMUM_BALANCE_EXCEED(CONFLICT.value(), "보유할 수 있는 최대 포인트 한도를 초과했습니다."),
    COUPON_STOCK_EXHAUSTED(CONFLICT.value(), "쿠폰이 모두 소진되었습니다."),
    INSUFFICIENT_STOCK(CONFLICT.value(), "상품 재고가 부족합니다."),
    INSUFFICIENT_BALANCE(CONTINUE.value(), "잔액이 부족합니다."),
    EXCEED_MAXIMUM_ORDER_PRODUCT(CONTINUE.value(), "한 번에 주문할 수 있는 최대 주문 상품 개수를 초과하였습니다.");

    private final Integer statusCode;
    private final String message;

    ErrorCode(String message) {
        this.statusCode = BAD_REQUEST.value();
        this.message = message;
    }

    ErrorCode(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(this);
    }
}
