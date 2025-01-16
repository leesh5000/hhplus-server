package kr.hhplus.be.server.product.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.domain.dto.response.PageDetails;
import kr.hhplus.be.server.product.domain.service.ProductService;
import kr.hhplus.be.server.product.domain.service.dto.response.ListProductsResult;
import kr.hhplus.be.server.product.interfaces.dto.response.ListProductsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "상품 API")
@RequiredArgsConstructor
@RestController
public class ListProductsController {

    private final ProductService service;

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다.",
            tags = {"상품 API"},
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "페이지 번호",
                            example = "1"
                    ),
                    @Parameter(
                            name = "size",
                            description = "페이지 크기",
                            example = "20"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품 목록 조회 성공",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = PageDetails.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "상품 목록 조회 실패"
                    )
            }
    )
    @GetMapping(value = "/api/v1/products", produces = "application/json")
    public ResponseEntity<PageDetails<List<ListProductsResponse>>> listProducts(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size
    ) {
        PageDetails<List<ListProductsResult>> pageDetails = service.list(page, size);

        List<ListProductsResponse> responses = pageDetails.content()
                .stream()
                .map(
                    ListProductsResponse::from
                )
                .toList();

        PageDetails<List<ListProductsResponse>> response = new PageDetails<>(
                pageDetails.page(),
                pageDetails.size(),
                pageDetails.totalElements(),
                pageDetails.totalPages(),
                responses
        );

        return ResponseEntity.ok(response);
    }
}
