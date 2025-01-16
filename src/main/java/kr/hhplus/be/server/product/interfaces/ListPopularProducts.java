package kr.hhplus.be.server.product.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.product.domain.service.ProductService;
import kr.hhplus.be.server.product.domain.service.dto.response.TopSellingProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "상품 API")
@RequiredArgsConstructor
@RestController
public class ListPopularProducts {

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
    @GetMapping(value = "/products/top-sales", produces = "application/json")
    public ResponseEntity<List<TopSellingProductResult>> listPopularProducts(
            @RequestParam(value = "day", defaultValue = "3") Integer day,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit
    ) {
        List<TopSellingProductResult> popularProducts = productService.getPopularProducts(day, limit);
        return ResponseEntity.ok(popularProducts);
    }
}
