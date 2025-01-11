package kr.hhplus.be.server.user.domain.service;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.mock.TestContainer;
import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.mock.domain.WalletFixture;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;
import kr.hhplus.be.server.user.domain.WalletHistory;
import kr.hhplus.be.server.user.domain.service.dto.request.UserPointCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserServiceTest {

    @DisplayName("유저 1000 포인트를 충전하면, " +
            "사용자의 포인트가 1000 만큼 증가하고, 유저 지갑 내역 크기가 1 증가해야 한다.")
    @Test
    void charge() {

        // Mocking
        TestContainer testContainer = new TestContainer();
        UserService sut = testContainer.userService;

        // given : ID가 1이고, 이름이 "항해99 유저"인 유저가 있을 때,
        User userFixture = UserFixture.create(1L);
        testContainer.userRepository.save(userFixture);

        Long userId = userFixture.getId();
        Point userPoint = userFixture.getPoint();
        int walletHistorySize = testContainer.userService
                .getWalletHistories(userId)
                .size();

        // when : 유저 1000 포인트를 충전하면
        int amount = 1000;
        UserPointCommand cmd = new UserPointCommand(userId, amount);
        sut.chargePoint(cmd);

        // then 1 : 사용자의 포인트가 1000 만큼 증가하고,
        Point actualUserPoint = userFixture.getPoint();
        Point expectedUserPoint = userPoint.add(amount);
        Assertions.assertThat(actualUserPoint).isEqualTo(expectedUserPoint);

        // then 2 : 유저 지갑 내역 크기가 1 증가해야 한다.
        int actualWalletHistorySize = testContainer.userService
                .getWalletHistories(userId)
                .size();
        Integer expectedWalletHistoriesSize = walletHistorySize + 1;
        Assertions.assertThat(
                        actualWalletHistorySize)
                .isEqualTo(expectedWalletHistoriesSize);
    }

    @DisplayName("지갑이 최대 한도인 유저가 충전을 시도하면, `BusinessException`이 발생하고 에러 코드가 `MAXIMUM_BALANCE_EXCEEDED`여야 한다.")
    @Test
    void charge_Fail() {

        // Mocking
        TestContainer testContainer = new TestContainer();
        UserService sut = testContainer.userService;

        // given : 보유할 수 있는 최대 포인트를 가진 유저가 있을 때,
        Point maximumBalance = Wallet.getMaximumBalance();
        User userFixture = UserFixture.create(1L, maximumBalance);
        testContainer.userRepository.save(userFixture);

        Long userId = userFixture.getId();

        // when & then : 유저가 1000 포인트를 충전하면, `BusinessException`이 발생하고 에러 코드가 `MAXIMUM_BALANCE_EXCEEDED`여야 한다.
        UserPointCommand cmd = new UserPointCommand(userId, 1000);
        Assertions.assertThatThrownBy(
                        () -> sut.chargePoint(cmd)
                )
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", ErrorCode.MAXIMUM_BALANCE_EXCEED
                );
    }

    @DisplayName("ID 1을 가진 지갑이 존재하고, 내역이 3개 존재할 때, " +
            "해당 유저의 지갑 내역을 조회하면, " +
            "3개의 지갑 내역과 ID가 일치해야 한다.")
    @Test
    void getWalletHistories() {

        // Mocking
        TestContainer testContainer = new TestContainer();
        User userFixture = UserFixture.create(1L);
        testContainer.userRepository.save(userFixture);
        UserService sut = testContainer.userService;

        // given : ID 1을 가진 유저 A의 지갑 내역이 3개 존재한다.
        Wallet walletFixture = WalletFixture.create(1L);
        testContainer.userRepository.save(
                new WalletHistory(1L, walletFixture, 100)
        );
        testContainer.userRepository.save(
                new WalletHistory(2L, walletFixture, -100)
        );
        testContainer.userRepository.save(
                new WalletHistory(3L, walletFixture, 3000)
        );

        // when : 해당 ID로 유저의 지갑 내역을 조회하면,
        Long walletId = walletFixture.getId();
        List<WalletHistory> userWalletHistories = sut.getWalletHistories(walletId);

        // then 1 : 총 개수가 같아야 하고
        Assertions.assertThat(userWalletHistories)
                .hasSize(3);

        // then 2 : 각각의 ID가 일치해야 한다.
        Assertions.assertThat(userWalletHistories)
                .extracting(WalletHistory::getId)
                .containsExactly(1L, 2L, 3L);
    }
}