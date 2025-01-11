package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.user.domain.Wallet;

public class WalletFixture {

    private WalletFixture() {
    }

    public static Wallet create(Long id) {
        return new Wallet(id);
    }
}
