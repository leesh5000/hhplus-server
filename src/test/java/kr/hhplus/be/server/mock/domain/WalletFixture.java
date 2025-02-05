package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;

public class WalletFixture {

    private WalletFixture() {
    }

    public static Wallet create(Long id) {
        User user = UserFixture.create(1L);
        return new Wallet(id, user);
    }
}
