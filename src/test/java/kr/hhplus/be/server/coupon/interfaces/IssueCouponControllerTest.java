package kr.hhplus.be.server.coupon.interfaces;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.mock.TestContainers;
import kr.hhplus.be.server.mock.domain.CouponFixture;
import kr.hhplus.be.server.mock.domain.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@DisplayName("컨트롤러 단위 테스트 : 쿠폰 발급")
class IssueCouponControllerTest {

    @DisplayName("""
            ID가 1인 사용자와 ID가 1인 쿠폰이 존재할 때,
            
            쿠폰 발급을 요청하면,
            
            {200 Created 상태코드} 와
            {Location 헤더에 쿠폰 발급 URL}을 응답해야 한다.
            """)
    @Test
    void issue() {

        // Mocking
        TestContainers testContainers = new TestContainers();
        IssueCouponController sut = testContainers.issueCouponController;
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/api/v1/users/1/coupons/1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // given : ID가 1인 사용자와 ID가 1인 쿠폰이 존재
        User user = UserFixture.create(1L);
        Coupon coupon = CouponFixture.create(1L);
        testContainers.userRepository.save(user);
        testContainers.couponRepository.save(coupon);

        // when : 쿠폰 발급을 요청하면
        ResponseEntity<Void> response = sut.issue(
                user.getId(),
                coupon.getId()
        );

        // then 1 : 201 Created 상태코와
        HttpStatusCode statusCode = response.getStatusCode();
        Assertions.assertThat(statusCode).isEqualTo(HttpStatusCode.valueOf(200));
        // then 2 : Location 헤더에 쿠폰 발급 URL 을 응답해야 한다.
        Assertions.assertThat(response.getHeaders().getFirst("Location")).isEqualTo("http://localhost/api/v1/users/1/coupons/1");

    }

}