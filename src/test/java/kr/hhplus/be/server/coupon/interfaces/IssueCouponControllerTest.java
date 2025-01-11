package kr.hhplus.be.server.coupon.interfaces;

import kr.hhplus.be.server.mock.TestContainer;
import kr.hhplus.be.server.mock.domain.CouponFixture;
import kr.hhplus.be.server.mock.domain.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@DisplayName("컨트롤러 단위 테스트 : 쿠폰 발급")
class IssueCouponControllerTest {

    @DisplayName("ID 1인 유저와 ID 1인 쿠폰이 존재할 때, " +
            "쿠폰 발급 API를 호출하면, " +
            "200 응답과 Location 헤더에 쿠폰을 조회할 수 있는 URL 반환해야 한다.")
    @Test
    void issue() {

        // Mocking
        TestContainer testContainer = new TestContainer();
        testContainer.userRepository.save(
                UserFixture.create(1L)
        );
        testContainer.couponRepository.save(
                CouponFixture.create(1L)
        );
        IssueCouponController sut = testContainer.issueCouponController;

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/api/v1/users/1/coupons/1");
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(mockRequest)
        );

        // given
        Long userId = 1L;
        Long couponId = 1L;

        // when
        ResponseEntity<Void> response = sut.issue(userId, couponId);

        // then
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getHeaders().getFirst("Location")).isEqualTo("http://localhost/api/v1/users/1/coupons/1");

    }

}