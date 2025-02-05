package kr.hhplus.be.server.coupon.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.coupon.domain.service.CouponService;
import kr.hhplus.be.server.coupon.interfaces.dto.response.ListCouponsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "쿠폰 API")
@RequiredArgsConstructor
@RestController
public class ListCouponsController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 목록 조회", description = "사용자의 쿠폰 목록을 조회합니다.",
            tags = {"쿠폰 API"},
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "userId",
                            description = "사용자 ID",
                            required = true
                    )
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "쿠폰 목록 조회 성공",
                            content = {
                                    @io.swagger.v3.oas.annotations.media.Content(
                                            array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ListCouponsResponse.class
                                            )
                                    )
                                )
                            }
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "쿠폰 목록 조회 실패"
                    )
            }
    )
    @GetMapping(path = "/api/v1/users/{userId}/coupons", produces = "application/json")
    public ResponseEntity<List<ListCouponsResponse>> listCoupons(
            @PathVariable("userId") Long userId
    ) {

        List<ListCouponsResponse> responses = couponService.listUserCoupons(userId)
                .stream()
                .map(ListCouponsResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
