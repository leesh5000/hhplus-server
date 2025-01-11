package kr.hhplus.be.server.payment.domain.external;

import java.time.LocalDateTime;

public interface DataPlatform {
    void sendPaymentData(Long orderId, Long usePoint, Long discountPoint, LocalDateTime paidAt);
}
