package kr.hhplus.be.server.order.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.order.application.OrderAndPayUseCase;
import kr.hhplus.be.server.order.application.dto.request.OrderAndPayCriteria;
import kr.hhplus.be.server.order.interfaces.dto.PlaceOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "주문/결제 API")
@RequiredArgsConstructor
@RestController
public class OrderAndPayController {

    private final OrderAndPayUseCase useCase;

    @Operation(summary = "주문/결제", description = "주문/결제 요청",
            tags = {"주문/결제 API"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "요청 본문",
                    required = true,
                    content = {
                            @Content(
                                    schema = @Schema(implementation = PlaceOrderRequest.class)
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주문 생성 성공",
                            headers = {
                                    @Header(
                                            name = "Location",
                                            description = "생성된 주문 URI",
                                            schema = @Schema(type = "string", example = "/api/v1/orders/1")
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "주문 생성 실패"
                    )
            }
    )
    @PostMapping("/api/v1/orders")
    public ResponseEntity<Void> orderAndPay(
            @RequestBody
            PlaceOrderRequest request
    ) {

        OrderAndPayCriteria criteria = request.toCriteria();
        Long orderId = useCase.orderAndPay(criteria);

        URI orderUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();

        return ResponseEntity
                .created(orderUri)
                .build();
    }
}
