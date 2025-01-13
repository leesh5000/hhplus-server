package kr.hhplus.be.server.user.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.user.domain.service.UserService;
import kr.hhplus.be.server.user.domain.service.dto.request.UsePointCommand;
import kr.hhplus.be.server.user.interfaces.dto.request.UserPointRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "사용자 API")
@RequiredArgsConstructor
@RestController
public class ChargePointController {

    private final UserService userService;

    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.",
            tags = {"사용자 API"},
            parameters = {
                    @Parameter(name = "userId", description = "사용자 ID", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 포인트 충전 요청",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserPointRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            headers = {
                                    @Header(
                                            name = "Location",
                                            description = "사용자 포인트 URI",
                                            example = "/api/v1/users/1/points",
                                            schema = @Schema(type = "string")
                                    )
                            },
                            description = "포인트 충전 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "포인트 충전 실패"
                    )
            }
    )
    @PostMapping(value = "/api/v1/users/{userId}/points", consumes = "application/json")
    public ResponseEntity<Void> charge(
            @PathVariable("userId") Long userId,
            @RequestBody UserPointRequest request
    ) {

        UsePointCommand command = request.toCommand(userId);
        userService.chargePoint(
                command
        );

        URI userPointsUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.ok()
                .header("Location", userPointsUri.toString())
                .build();
    }


}
