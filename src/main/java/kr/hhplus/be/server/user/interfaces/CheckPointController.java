package kr.hhplus.be.server.user.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.user.domain.service.UserService;
import kr.hhplus.be.server.user.domain.service.dto.response.CheckPointDetail;
import kr.hhplus.be.server.user.interfaces.dto.response.CheckPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 API")
@RequiredArgsConstructor
@RestController
public class CheckPointController {

    private final UserService userService;

    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.",
            tags = {"사용자 API"},
            parameters = {
                    @Parameter(name = "userId", description = "사용자 ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "포인트 조회 성공",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = CheckPointResponse.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "포인트 조회 실패"
                    )
            }
    )
    @GetMapping("/api/v1/users/{userId}/points")
    public ResponseEntity<CheckPointResponse> check(
            @PathVariable("userId") Long userId
    ) {
        CheckPointDetail result = userService.checkPoint(
                userId
        );
        CheckPointResponse response = CheckPointResponse.from(result);
        return ResponseEntity.ok(response);
    }
}
