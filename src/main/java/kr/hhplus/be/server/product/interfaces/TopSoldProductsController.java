package kr.hhplus.be.server.product.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.product.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 API")
@RequiredArgsConstructor
@RestController
public class TopSoldProductsController {

    private final ProductService productService;

    @Operation(summary = "인기 상품 조회", description = "최근 N일 동안의 판매량 상위 M개 까지의 상품을 조회합니다.",
            tags = {"상품 API"},
            parameters = {
                    @Parameter(
                            name = "day",
                            description = "일 수",
                            required = false,
                            example = "3"
                    ),
                    @Parameter(
                            name = "limit",
                            description = "조회 개수",
                            required = false,
                            example = "5"
                    )
            }
    )
    @GetMapping(value = "/products/top-sold", produces = "application/json")
    public void topSoldProducts(
            @RequestParam(value = "day", defaultValue = "3") Integer day,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ) {
        productService.getPopularProducts(day, limit);
    }
}
