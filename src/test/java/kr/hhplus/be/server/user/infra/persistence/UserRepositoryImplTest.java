package kr.hhplus.be.server.user.infra.persistence;

import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.infra.persistence.jpa.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryImplTest {

    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("유저 저장 테스트")
    void save() {

        // given
        User user = UserFixture.create();

        // when
        User saveUser = userJpaRepository.save(user);

        // then
        Long saveUserId = saveUser.getId();
        String saveUserName = saveUser.getName();
        userJpaRepository.findById(saveUserId).ifPresentOrElse(
                findUser -> {

                    Long findUserId = findUser.getId();
                    assertThat(findUserId).isEqualTo(saveUserId);

                    String findUserName = findUser.getName();
                    assertThat(findUserName).isEqualTo(saveUserName);
                },
                () -> {
                    throw new AssertionError("유저를 찾을 수 없습니다.");
                }
        );
    }

}