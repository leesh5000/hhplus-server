package kr.hhplus.be.server.order.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderProduct;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.service.dto.request.PlaceOrderCommand;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order placeOrder(PlaceOrderCommand command) {

        List<OrderProduct> orderProducts = generateOrderProducts(command);
        User orderer = command.orderer();
        Order order = new Order(orderer, orderProducts);
        return orderRepository.save(order);
    }

    private List<OrderProduct> generateOrderProducts(PlaceOrderCommand command) {

        List<OrderProduct> orderProducts = new ArrayList<>();

        command.orderProducts().forEach(orderProductCommand -> {
            Long productId = orderProductCommand.productId();
            Integer orderQuantity = orderProductCommand.quantity();

            Product product = productRepository.getById(productId);
            product.decreaseStock(orderQuantity);

            OrderProduct orderProduct = new OrderProduct(orderQuantity, product);
            orderProducts.add(orderProduct);
        });
        return orderProducts;
    }
}
