package kr.hhplus.be.server.payment.domain.service.dto.request;

import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.order.domain.Order;

public record PayCommand(
        Order order,
        Point usePoint,
        Point discountPoint
) {


}
