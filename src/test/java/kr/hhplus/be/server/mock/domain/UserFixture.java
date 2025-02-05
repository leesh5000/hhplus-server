package kr.hhplus.be.server.mock.domain;

import kr.hhplus.be.server.user.domain.User;

public class UserFixture {

    private UserFixture() {
    }

    public static User create() {
        return new User(null, "유저");
    }

    public static User create(Long id) {
        return new User(id, "유저");
    }

    public static User create(Long id, Long initialPoint) {
        return new User(id, "유저", initialPoint);
    }
}
