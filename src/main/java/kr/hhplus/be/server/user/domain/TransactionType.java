package kr.hhplus.be.server.user.domain;

import lombok.Getter;

@Getter
public enum TransactionType {

    DEPOSIT(1), 
    WITHDRAW(-1);

    private final int sign;

    TransactionType(int sign) {
        this.sign = sign;
    }
}
