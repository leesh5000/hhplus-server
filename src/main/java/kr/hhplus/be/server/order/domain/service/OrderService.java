package kr.hhplus.be.server.order.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.service.dto.request.PlaceOrderCommand;
import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public Order placeOrder(User orderer, List<PlaceOrderCommand> commands) {
        Order order = new Order(orderer);
        commands.forEach(cmd -> order
                .addProduct(cmd.product(), cmd.quantity()));
        return orderRepository.save(order);
    }

    public Order getById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}
