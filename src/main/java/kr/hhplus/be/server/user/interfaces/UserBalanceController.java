package kr.hhplus.be.server.user.interfaces;

import kr.hhplus.be.server.user.interfaces.dto.request.ChargeUserBalanceRequest;
import kr.hhplus.be.server.user.interfaces.dto.response.UserBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class UserBalanceController {

    /**
     * 사용자 잔액 충전 API
     *
     * @param userId    사용자 ID
     * @param request   충전 금액 요청 DTO
     * @return 잔액 충전 결과
     */
    @PostMapping(value = "/api/v1/users/{userId}/balances", consumes = "application/json")
    public ResponseEntity<Void> charge(
            @PathVariable("userId") Long userId,
            @RequestBody ChargeUserBalanceRequest request
    ) {

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.ok()
                .header("Location", uri.toString())
                .build();
    }

    /**
     * 사용자 잔액 조회 API
     *
     * @param userId 사용자 ID
     * @return 현재 잔액 정보
     */
    @GetMapping("/api/v1/users/{userId}/balances")
    public ResponseEntity<UserBalanceResponse> get(
            @PathVariable("userId") Long userId
    ) {
        // Mocking
        UserBalanceResponse response = new UserBalanceResponse("1", 1000);
        return ResponseEntity.ok(response);
    }

}
