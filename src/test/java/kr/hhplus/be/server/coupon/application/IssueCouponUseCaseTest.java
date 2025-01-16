package kr.hhplus.be.server.coupon.application;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.mock.TestContainers;
import kr.hhplus.be.server.mock.domain.CouponFixture;
import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static kr.hhplus.be.server.common.domain.ErrorCode.COUPON_STOCK_EXHAUSTED;
import static kr.hhplus.be.server.common.domain.ErrorCode.EXPIRED_COUPON;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("유스케이스 단위 테스트 : 쿠폰 발급")
class IssueCouponUseCaseTest {

    @DisplayName("""                  
            ID가 1인 사용자와 ID가 1인 쿠폰이 존재할 때,
            
            사용자에게 쿠폰을 발급하면,
            
            사용자의 {보유 쿠폰 개수가 1 증가} 해야한다.
            """)
    @Test
    void issue() {

        // Mocking
        LocalDateTime now = LocalDateTime.now();
        TestContainers testContainers = new TestContainers(() -> now);
        IssueCouponUseCase sut = new IssueCouponUseCase(
                testContainers.userService,
                testContainers.couponService);

        // given : ID가 1인 사용자와 ID가 1인 쿠폰이 존재
        User user = UserFixture.create(1L);
        Coupon coupon = CouponFixture.create(1L);
        testContainers.userRepository.save(user);
        testContainers.couponRepository.save(coupon);
        Long userCouponsSize = testContainers.couponRepository.countIssuedCouponsByUserId(1L);

        // when : 사용자에게 쿠폰을 발급하면
        sut.issue(
                user.getId(),
                coupon.getId()
        );

        // then 1 : 사용자의 보유 쿠폰 개수가 1 증가하고,
        Long updatedUserCouponsSize = testContainers.couponRepository.countIssuedCouponsByUserId(1L);
        assertThat(updatedUserCouponsSize).isEqualTo(userCouponsSize + 1);
    }

    @DisplayName("""
            재고가 없는 쿠폰을
            
            발급 요청하면,
            
            {BusinessException}이 발생하고,
            에러 코드가 {COUPON_STOCK_EXHAUSTED}여야 한다.
            """)
    @Test
    void issue_CouponStockExhausted() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        User user = UserFixture.create(1L);
        testContainers.userRepository.save(user);
        IssueCouponUseCase sut = new IssueCouponUseCase(
                testContainers.userService,
                testContainers.couponService
        );

        // given : 재고가 없는 쿠폰이 존재
        Coupon coupon = CouponFixture.create(1L, 0);
        testContainers.couponRepository.save(coupon);

        assertThatThrownBy(
            // when : 발급 요청하면
            () -> sut.issue(
                user.getId(),
                coupon.getId()
        ))

                // then : {BusinessException}이 발생하고
                .isInstanceOf(BusinessException.class)
                // then : 에러 코드가 {COUPON_STOCK_EXHAUSTED}여야 한다.
                .hasFieldOrPropertyWithValue("errorCode", COUPON_STOCK_EXHAUSTED);
    }

    @DisplayName("""
            이미 만료된 쿠폰을
            
            발급 시도하면,
            
            {BusinessException}이 발생하고,
            에러 코드가 {EXPIRED_COUPON}여야 한다.
            """)
    @Test
    void issue_CouponExpired() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        User user = UserFixture.create(1L);
        testContainers.userRepository.save(user);
        IssueCouponUseCase sut = new IssueCouponUseCase(
                testContainers.userService,
                testContainers.couponService
        );

        // given : 이미 만료된 쿠폰이 존재
        LocalDateTime expiredAt = LocalDateTime.now()
                .minusMonths(1);
        Coupon coupon = CouponFixture.create(1L, expiredAt);
        testContainers.couponRepository.save(coupon);

        assertThatThrownBy(
            // when : 발급 시도하면
            () -> sut.issue(
                user.getId(),
                coupon.getId()
        ))
                // then : {BusinessException}이 발생하고
                .isInstanceOf(BusinessException.class)
                // then : 에러 코드가 {EXPIRED_COUPON}여야 한다.
                .hasFieldOrPropertyWithValue("errorCode", EXPIRED_COUPON);
    }
}