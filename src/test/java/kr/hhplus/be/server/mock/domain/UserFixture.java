package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;

public class UserFixture {

    private UserFixture() {
    }

    public static User create(Long id) {
        Wallet wallet = WalletFixture.create(1L);
        return new User(id, "유저", wallet);
    }

    public static User create(Long id, Long initialPoint) {
        return new User(id, "유저", initialPoint);
    }

    public static User create(Long id, Point maximumBalance) {
        return new User(id, "유저", maximumBalance.amount());
    }

    public static User create(Long id, Wallet userWallet) {
        return new User(id, "유저", userWallet);
    }
}
