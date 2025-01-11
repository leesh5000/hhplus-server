package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.mock.domain.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도메인 단위 테스트 : 사용자")
class UserTest {

    @DisplayName("포인트를 충전하면, 사용자의 포인트가 충전량 만큼 증가하고, 충전량을 내역으로 가지는 지갑 내역을 반환해야 한다.")
    @Test
    void chargePoint() {
        // given
        User user = UserFixture.create(1L, 0L);
        int amount = 1000;

        // when
        WalletHistory walletHistory = user.chargePoint(amount);

        // then
        Point actualUserPoint = user.getPoint();
        Assertions.assertThat(actualUserPoint.amount()).isEqualTo(amount);
        Integer walletHistoryAmount = walletHistory.getAmount();
        Assertions.assertThat(walletHistoryAmount).isEqualTo(amount);
    }

    @DisplayName("사용자는 자신의 현재 포인트를 조회할 수 있다.")
    @Test
    void getPoint() {
        // given
        User user = UserFixture.create(1L, 1000L);

        // when
        Point point = user.getPoint();

        // then
        Assertions.assertThat(point.amount()).isEqualTo(1000);
    }
}