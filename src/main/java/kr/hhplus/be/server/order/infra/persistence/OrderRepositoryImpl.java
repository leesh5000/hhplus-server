package kr.hhplus.be.server.order.infra.persistence;

import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.infra.persistence.jpa.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    private final OrderJpaRepository orderJpaRepository;
}
