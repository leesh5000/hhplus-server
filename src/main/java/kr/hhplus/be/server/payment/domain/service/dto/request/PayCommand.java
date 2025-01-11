package kr.hhplus.be.server.payment.domain.service.dto.request;

import java.util.List;

public class PayCommand {
        Long orderId;
        List<Long> applyCouponIds;
        Long amount;
        Integer discountAmount;


}
