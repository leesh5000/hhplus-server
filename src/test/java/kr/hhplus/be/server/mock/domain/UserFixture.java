package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;

public class UserFixture {

    private UserFixture() {
    }

    public static User create(Long id) {
        return new User(id, "유저", 0L);
    }

    public static User create(Long id, Long initialPoint) {
        Wallet wallet = WalletFixture.create(1L, initialPoint);
        return new User(id, "유저", wallet);
    }

    public static User create(Long id, Point maximumBalance) {
        return new User(id, "유저", maximumBalance.toLong());
    }
}
