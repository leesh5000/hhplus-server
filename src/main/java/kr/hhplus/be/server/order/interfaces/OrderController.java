package kr.hhplus.be.server.order.interfaces;

import kr.hhplus.be.server.order.application.PlaceOrderUseCase;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }
}
