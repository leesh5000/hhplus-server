package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.mock.domain.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 : 사용자")
class UserTest {

    @DisplayName("""
            ID가 1이고, 초기 잔고가 0원인 사용자가 존재하고,
            
            1000원을 충전하면,
            
            사용자의 포인트가 {1000 만큼 증가}하고,
            amount 가 {1000인 지갑 내역}을 응답해야 한다.
            """)
    @Test
    void chargePoint() {

        // given : ID가 1이고, 초기 잔고가 0원인 사용자가 존재
        User user = UserFixture.create(1L, 0L);

        // when : 1000원을 충전하면
        WalletHistory walletHistory = user.chargePoint(1000);

        // then 1 : 사용자의 포인트가 1000 만큼 증가하고,
        Assertions.assertThat(user.getPoint().toLong()).isEqualTo(1000);

        // then 2 : amount 가 1000인 지갑 내역을 응답해야 한다.
        Assertions.assertThat(walletHistory.getAmount()).isEqualTo(1000);

    }

    @DisplayName("""
            ID가 1이고, 초기 잔고가 1000원인 사용자가 존재하고,
            
            1000원을 빼면,
            
            사용자의 포인트가 {1000 만큼 감소}하고,
            amount 가 {-1000인 지갑 내역}을 응답해야 한다.
            """)
    @Test
    void withdrawPoint() {

        // given : ID가 1이고, 초기 잔고가 1000원인 사용자가 존재
        User user = UserFixture.create(1L, 1000L);

        // when : 1000원을 빼면
        WalletHistory walletHistory = user.deductPoint(1000);

        // then 1 : 사용자의 포인트가 1000 만큼 감소하고,
        Assertions.assertThat(user.getPoint().toLong()).isEqualTo(0);

        // then 2 : amount 가 -1000인 지갑 내역을 응답해야 한다.
        Assertions.assertThat(walletHistory.getAmount()).isEqualTo(-1000);

    }

}