package kr.hhplus.be.server.order.application;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.order.application.dto.request.PlaceOrderCriteria;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.service.OrderService;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.service.PaymentService;
import kr.hhplus.be.server.user.domain.service.UserService;
import kr.hhplus.be.server.user.domain.service.dto.request.UserPointCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class PlaceOrderUseCase {

    private final OrderService orderService;
    private final CouponService couponService;
    private final PaymentService paymentService;
    private final UserService userService;

    public void placeOrder(PlaceOrderCriteria criteria) {

        // 주문을 생성한다.
        Order order = orderService.placeOrder(
                criteria.toOrderCommand()
        );

        // 할인 금액을 계산한다.
        Point totalDiscountPoints = couponService.calculateDiscountPoint(criteria.issuedCouponIds());


        // 유저의 포인트를 차감한다.
        Point usePoint = order.getOrderPrice().subtract(totalDiscountPoints);
        UserPointCommand command = new UserPointCommand(
                criteria.userId(),
                usePoint.toInt());
        userService.usePoint(
                command
        );

        // 결제를 처리한다.
        Payment payment = paymentService.pay(
                order.getId(),
                usePoint,
                totalDiscountPoints);

        // 할인 쿠폰을 사용 처리한다.
        couponService.useCoupons(
                criteria.issuedCouponIds(),
                payment.getId()
        );

    }
}
