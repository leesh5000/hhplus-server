package kr.hhplus.be.server.user.interfaces.dto.response;

import kr.hhplus.be.server.user.domain.service.dto.response.CheckPointDetail;

public record CheckPointResponse(Long point) {
    public static CheckPointResponse from(CheckPointDetail result) {
        return new CheckPointResponse(
            result.point()
        );
    }
}
