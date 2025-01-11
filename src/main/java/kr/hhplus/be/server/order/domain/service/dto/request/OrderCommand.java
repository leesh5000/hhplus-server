package kr.hhplus.be.server.order.domain.service.dto.request;

import java.util.List;

public record OrderCommand(Long userId, List<OrderProductCommand> products) {

    public record OrderProductCommand(Long productId, Integer quantity) {
    }
}
