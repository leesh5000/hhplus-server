package kr.hhplus.be.server.user.domain.service;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.mock.TestContainers;
import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.service.dto.request.ChargePointCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kr.hhplus.be.server.user.domain.Wallet.MAXIMUM_BALANCE;

@DisplayName("서비스 단위 테스트 : 사용자")
class UserServiceTest {

    @DisplayName("""
            ID가 1인 사용자의
            
            포인트를 1000 충전하면,
            
            사용자의 포인트가 {1000 만큼 증가}하고,
            {유저 지갑 내역 크기가 1 증가}해야 한다.
            """)
    @Test
    void charge() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        UserService sut = testContainers.userService;

        // given : ID가 1인 사용자가 존재
        User user = UserFixture.create(1L, 0L);
        testContainers.userRepository.save(user);
        int walletHistorySize = testContainers.userRepository.findAllWalletHistories(user).size();

        // when : 유저 1000 포인트를 충전하면
        ChargePointCommand cmd = new ChargePointCommand(1L, 1000);
        sut.chargePoint(cmd);

        // then 1 : 사용자의 포인트가 1000 만큼 증가하고,
        User updatedUser = testContainers.userRepository.getById(1L);
        Assertions.assertThat(updatedUser.getPoint()).isEqualTo(1000);

        // then 2 : 유저 지갑 내역 크기가 1 증가해야 한다.
        int updatedWalletHistoriesSize = testContainers.userRepository.findAllWalletHistories(user).size();
        Assertions.assertThat(updatedWalletHistoriesSize).isEqualTo(walletHistorySize + 1);
    }

    @DisplayName("""
            보유 포인트가 최대 한도인 유저의
            
            포인트를 충전하면,
            
            {BusinessException}이 발생하고
            에러 코드가 {MAXIMUM_BALANCE_EXCEEDED}여야 한다.
            """)
    @Test
    void charge_Fail() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        UserService sut = testContainers.userService;

        // given : 보유 포인트가 최대 한도인 유저가 존재
        User user = UserFixture.create(1L, MAXIMUM_BALANCE);
        testContainers.userRepository.save(user);

        // when : 유저 1000 포인트를 충전하면
        ChargePointCommand cmd = new ChargePointCommand(1L, 1000);
        Assertions.assertThatThrownBy(
                        () -> sut.chargePoint(cmd)
                )

                // then 1 : {BusinessException}이 발생하고
                .isInstanceOf(BusinessException.class)
                // then 2 : 에러 코드가 {MAXIMUM_BALANCE_EXCEEDED}여야 한다.
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MAXIMUM_BALANCE_EXCEED);
    }
}