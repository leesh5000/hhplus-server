package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.Order;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long orderId);
    Long count();
}
