package kr.hhplus.be.server.common.domain;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_AMOUNT_REQUEST("충전 요청 금액이 유효하지 않습니다."),
    RESOURCE_NOT_FOUND("해당 리소스를 찾을 수 없습니다."),
    MAXIMUM_BALANCE_EXCEED("보유할 수 있는 최대 포인트 한도를 초과했습니다."),
    COUPON_STOCK_EXHAUSTED("쿠폰이 모두 소진되었습니다."),
    EXPIRED_COUPON("이미 만료일이 지난 쿠폰입니다."),
    INSUFFICIENT_STOCK("상품 재고가 부족합니다."),
    ALREADY_USED_COUPON("이미 사용한 쿠폰입니다."),

    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
