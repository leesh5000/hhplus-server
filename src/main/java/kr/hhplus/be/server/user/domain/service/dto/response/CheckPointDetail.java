package kr.hhplus.be.server.user.domain.service.dto.response;

import kr.hhplus.be.server.user.domain.User;

public record CheckPointDetail(Long userId, Long point) {
    public static CheckPointDetail from(User user) {
        return new CheckPointDetail(
                user.getId(),
                user.getPoint().amount()
        );
    }
}
