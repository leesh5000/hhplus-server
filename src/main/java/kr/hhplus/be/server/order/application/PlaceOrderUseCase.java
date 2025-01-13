package kr.hhplus.be.server.order.application;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.IssuedCoupon;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.coupon.domain.service.dto.request.UseCouponCommand;
import kr.hhplus.be.server.order.application.dto.request.PlaceOrderCriteria;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.service.OrderService;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.service.PaymentService;
import kr.hhplus.be.server.payment.domain.service.dto.request.PayCommand;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.service.UserService;
import kr.hhplus.be.server.user.domain.service.dto.request.UsePointCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PlaceOrderUseCase {

    private final OrderService orderService;
    private final CouponService couponService;
    private final PaymentService paymentService;
    private final UserService userService;

    public void placeOrder(PlaceOrderCriteria criteria) {

        // 사용자를 조회한다.
        User orderer = userService.getById(
                criteria.userId()
        );

        // 주문을 생성한다.
        Order order = orderService.placeOrder(
                criteria.toPlaceOrderCommand(orderer)
        );

        // 할인 금액을 계산한다.
        List<IssuedCoupon> issuedCoupons = couponService.getIssuedCouponsByIds(
                criteria.issuedCouponIds()
        );
        Point totalDiscountPoints = couponService.calculateDiscountPoint(issuedCoupons);

        // 유저의 포인트를 차감한다.
        Point usePoint = order.getOrderPrice().subtract(totalDiscountPoints);
        UsePointCommand command = new UsePointCommand(
                orderer,
                usePoint.toInt());
        userService.usePoint(command);

        // 결제를 처리한다.
        PayCommand payCommand = new PayCommand(
                order,
                usePoint,
                totalDiscountPoints);
        Payment payment = paymentService.pay(payCommand);

        // 할인 쿠폰을 사용 처리한다.
        UseCouponCommand useCouponCommand = new UseCouponCommand(
                orderer,
                payment,
                issuedCoupons
        );
        couponService.useCoupon(useCouponCommand);

    }
}
