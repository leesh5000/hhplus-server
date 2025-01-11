package kr.hhplus.be.server.coupon.application;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.mock.TestContainer;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("유스케이스 단위 테스트 : 쿠폰 발급")
class IssueCouponUseCaseTest {

    @DisplayName("유저 ID와 쿠폰 ID를 입력받아 쿠폰을 발급할 수 있다.")
    @Test
    void issue() {

        // Mocking
        TestContainer testContainer = new TestContainer();

        User userFixture = new User(1L, "유저 A");
        testContainer.userRepository.save(userFixture);

        LocalDateTime expiredAt = LocalDateTime.now()
                .plusMonths(1);
        Coupon couponFixture = new Coupon(1L,
                "쿠폰 A",
                1000L,
                10,
                expiredAt);
        testContainer.couponRepository.save(couponFixture);

        IssueCouponUseCase sut = new IssueCouponUseCase(
                testContainer.userService,
                testContainer.couponService
        );

        // given
        Long userId = 1L;
        Long couponId = 1L;

        // when
        sut.issue(userId, couponId);

        // then
        testContainer.couponRepository.getIssuedCoupons(userId)
                .stream()
                .filter(issuedCoupon -> issuedCoupon.getCouponId().equals(couponId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("발급된 쿠폰이 존재하지 않습니다."));
    }

    @DisplayName("쿠폰 재고가 없을 경우, 코드가 `COUPON_STOCK_EXHAUSTED`인, `BusinessException`이 발생한다.")
    @Test
    void issue_CouponStockExhausted() {

        // Mocking
        TestContainer testContainer = new TestContainer();

        User userFixture = new User(1L, "유저 A");
        testContainer.userRepository.save(userFixture);

        LocalDateTime expiredAt = LocalDateTime.now()
                .plusMonths(1);
        Coupon couponFixture = new Coupon(1L,
                "쿠폰 A",
                1000L,
                0,
                expiredAt);
        testContainer.couponRepository.save(couponFixture);

        IssueCouponUseCase sut = new IssueCouponUseCase(
                testContainer.userService,
                testContainer.couponService
        );

        // given
        Long userId = 1L;
        Long couponId = 1L;

        // when & then
        assertThatThrownBy(() -> sut.issue(userId, couponId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", kr.hhplus.be.server.common.domain.ErrorCode.COUPON_STOCK_EXHAUSTED);
    }

    @DisplayName("이미 만료된 쿠폰을 발급 시도하면, 코드가 `EXPIRED_COUPON`인, `BusinessException`이 발생한다.")
    @Test
    void issue_CouponExpired() {

        // Mocking
        TestContainer testContainer = new TestContainer();

        User userFixture = new User(1L, "유저 A");
        testContainer.userRepository.save(userFixture);

        LocalDateTime expiredAt = LocalDateTime.now()
                .minusMonths(1);
        Coupon couponFixture = new Coupon(1L,
                "쿠폰 A",
                1000L,
                10,
                expiredAt);
        testContainer.couponRepository.save(couponFixture);

        IssueCouponUseCase sut = new IssueCouponUseCase(
                testContainer.userService,
                testContainer.couponService
        );

        // given
        Long userId = 1L;
        Long couponId = 1L;

        // when & then
        assertThatThrownBy(() -> sut.issue(userId, couponId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("code", ErrorCode.EXPIRED_COUPON);
    }
}