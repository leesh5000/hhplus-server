package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.mock.domain.WalletFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 : 지갑 & 지갑 내역")
class WalletTest {

    @DisplayName("""
            ID가 1인 지갑이 존재하고,
            
            1000원을 입금하면,
            
            지갑의 잔고가 {1000원}이 되고,
            {1000원인 지갑 내역}을 응답해야 한다.
            """)
    @Test
    void charge() {

        // given : ID가 1인 지갑이 존재
        Wallet wallet = WalletFixture.create(1L);

        // when : 1000원을 충전하면
        WalletHistory walletHistory = wallet.deposit(1000);

        // then 1 : 지갑의 잔고가 1000원이 되고,
        Assertions.assertThat(wallet.getBalance().toLong()).isEqualTo(1000);

        // then 2 : 1000원인 지갑 내역을 응답해야 한다.
        Assertions.assertThat(walletHistory.getAmount()).isEqualTo(1000);
    }

    @DisplayName("""
            최초 지갑 생성 시에는,
            
            잔고는 {0원}이어야 한다.
            """)
    @Test
    void getBalance() {
        // given
        Wallet wallet = WalletFixture.create(1L);

        // when
        Point balance = wallet.getBalance();

        // then
        Assertions.assertThat(balance.toLong()).isEqualTo(0);
    }

    @DisplayName("""
            ID가 1인 지갑이 존재하고,
            
            1000원을 입금 후, 1000원을 출금하면,
            
            지갑의 잔고가 {0원}이 되고,
            {-1000원인 지갑 내역}을 응답해야 한다.
            """)
    @Test
    void withdraw() {

        // given : ID가 1인 지갑이 존재
        Wallet wallet = WalletFixture.create(1L);

        // when : 1000원을 충전하고, 1000원을 출금하면
        wallet.deposit(1000);
        WalletHistory walletHistory = wallet.withdraw(1000);

        // then 1 : 지갑의 잔고가 0원이 되고,
        Assertions.assertThat(wallet.getBalance().toLong()).isEqualTo(0);

        // then 2 : -1000원인 지갑 내역을 응답해야 한다.
        Assertions.assertThat(walletHistory.getAmount()).isEqualTo(-1000);
    }

    @DisplayName("""
            ID가 1이고, 잔고가 0원인 지갑이 존재하고,
            
            1000원을 인출하면,
            
            {BusinessException}이 발생하고,
            에러 코드가 {INSUFFICIENT_BALANCE}여야 한다.
            """)
    @Test
    void withdraw_Fail() {

        // given : ID가 1이고, 잔고가 0원인 지갑이 존재
        Wallet wallet = WalletFixture.create(1L);

        Assertions.assertThatThrownBy(
                // when : 1000원을 인출하면
                () -> wallet.withdraw(1000)
                )
                // then 1 : {BusinessException}이 발생하고,
                .isInstanceOf(BusinessException.class)
                // then 2 : 에러 코드가 {INSUFFICIENT_BALANCE}여야 한다.
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INSUFFICIENT_BALANCE);
    }
}