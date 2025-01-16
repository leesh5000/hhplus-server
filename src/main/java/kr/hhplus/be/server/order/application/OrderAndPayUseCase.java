package kr.hhplus.be.server.order.application;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.order.application.dto.request.OrderAndPayCriteria;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.service.OrderService;
import kr.hhplus.be.server.order.domain.service.dto.request.PlaceOrderCommand;
import kr.hhplus.be.server.payment.domain.service.PaymentService;
import kr.hhplus.be.server.payment.domain.service.dto.request.PayCommand;
import kr.hhplus.be.server.product.domain.service.ProductService;
import kr.hhplus.be.server.product.domain.service.dto.request.PrepareProductCommand;
import kr.hhplus.be.server.product.domain.service.dto.response.PrepareProductResult;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderAndPayUseCase {

    private final OrderService orderService;
    private final CouponService couponService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ProductService productService;

    public Long orderAndPay(OrderAndPayCriteria criteria) {

        // 사용자를 조회한다.
        Long userId = criteria.userId();
        User orderer = userService.getById(userId);

        // 주문할 상품을 재고에서 준비한다.
        List<PrepareProductCommand> commands = criteria.toPrepareProductCommands();
        List<PrepareProductResult> prepareProducts = productService.prepareAll(commands);

        // 주문을 생성한다.
        List<PlaceOrderCommand> placeOrderCommands = prepareProducts.stream().map(
                        result -> new PlaceOrderCommand(
                                result.product(),
                                result.quantity()
                        )).toList();
        Order order = orderService.placeOrder(orderer, placeOrderCommands);

        // 쿠폰을 사용 처리한다.
        List<Long> applyCouponIds = criteria.applyCouponIds();
        Point discountPoint = couponService.useCoupon(orderer, applyCouponIds);

        // [주문 금액 - 할인 금액] 만큼 사용자의 포인트를 사용 처리한다.
        Point usePoint = order.getOrderPrice().subtract(discountPoint);
        userService.usePoint(orderer, usePoint);

        // 결제 금액을 지불한다.
        PayCommand payCommand = new PayCommand(
                order,
                usePoint,
                discountPoint);
        paymentService.pay(payCommand);

        return order.getId();
    }
}
