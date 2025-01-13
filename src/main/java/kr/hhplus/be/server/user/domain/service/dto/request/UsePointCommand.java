package kr.hhplus.be.server.user.domain.service.dto.request;

import kr.hhplus.be.server.user.domain.User;

public record UsePointCommand(User user, Integer amount) {
}
