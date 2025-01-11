package kr.hhplus.be.server.coupon.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.coupon.application.IssueCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "쿠폰 API")
@RequiredArgsConstructor
@RestController
public class IssueCouponController {

    private final IssueCouponUseCase issueCouponUseCase;

    @Operation(summary = "쿠폰 발급", description = "사용자에게 쿠폰을 발급합니다.",
            tags = {"쿠폰 API"},
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "사용자 ID",
                            required = true
                    ),
                    @Parameter(
                            name = "couponId",
                            description = "쿠폰 ID",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "쿠폰 발급 성공",
                            headers = {
                                    @Header(
                                            name = "Location",
                                            description = "발급된 쿠폰 URI",
                                            schema = @Schema(type = "string", example = "/api/v1/users/1/coupons/1")
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "쿠폰 발급 실패"
                    )
            }
    )
    @PostMapping("/api/v1/users/{userId}/coupons/{couponId}")
    public ResponseEntity<Void> issue(
            @PathVariable("userId") Long userId,
            @PathVariable("couponId") Long couponId
    ) {

        issueCouponUseCase.issue(userId, couponId);

        URI userCouponsUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        // Issue user coupon
        return ResponseEntity.ok()
                .header("Location", userCouponsUri.toString())
                .build();
    }

}
