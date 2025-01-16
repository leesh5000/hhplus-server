package kr.hhplus.be.server.payment.domain.service.dto.request;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.order.domain.Order;

public record PayCommand(
        Order order,
        Point usePoint,
        Point discountPoint) {

    public PayCommand {
        if (usePoint.isNegative() || discountPoint.isNegative()) {
            throw new IllegalArgumentException("포인트는 음수가 될 수 없습니다.");
        }
    }
}
