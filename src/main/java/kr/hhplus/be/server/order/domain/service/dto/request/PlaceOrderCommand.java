package kr.hhplus.be.server.order.domain.service.dto.request;

import kr.hhplus.be.server.user.domain.User;

import java.util.List;

public record PlaceOrderCommand(User orderer, List<OrderProduct> orderProducts) {

    public record OrderProduct(Long productId, Integer quantity) {
    }
}
