package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.common.domain.Point;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 : 지갑 & 지갑 내역")
class WalletTest {

    @DisplayName("포인트를 입력하여, 지갑 잔고를 충전하면, 입력한 값 만큼 잔고가 추가되고 지갑 내역은 입력한 포인트를 양수로 가져야 한다.")
    @Test
    void charge() {
        // given
        Wallet wallet = new Wallet(1L);
        int amount = 1000;

        // when
        WalletHistory walletHistory = wallet.deposit(amount);

        // then
        Long actualWalletBalance = wallet.getBalance().amount();
        int actualWalletHistoryAmount = walletHistory.getAmount();
        Assertions.assertThat(actualWalletBalance).isEqualTo(amount);
        Assertions.assertThat(actualWalletHistoryAmount).isEqualTo(amount);
    }

    @DisplayName("최초 지갑 생성 시에는 잔고는 0원 이어야 한다.")
    @Test
    void getBalance() {
        // given
        Wallet wallet = new Wallet(1L);

        // when
        Point balance = wallet.getBalance();

        // then
        Assertions.assertThat(balance.amount()).isEqualTo(0);
    }

    @DisplayName("포인트를 입력하여, 지갑에서 잔고를 인출하면, 입력한 값 만큼 잔고가 차감되고 지갑 내역은 입력한 포인트를 음수로 가져야한다.")
    @Test
    void withdraw() {
        // given
        Wallet wallet = new Wallet(1L);
        int amount = 1000;
        wallet.deposit(amount);

        // when
        WalletHistory walletHistory = wallet.withdraw(amount);

        // then
        Long actualWalletBalance = wallet.getBalance().amount();
        int actualWalletHistoryAmount = walletHistory.getAmount();
        Assertions.assertThat(actualWalletBalance).isEqualTo(0);
        Assertions.assertThat(actualWalletHistoryAmount).isEqualTo(-amount);
    }
}