package kr.hhplus.be.server.payment.infra.dataplatform;

import kr.hhplus.be.server.payment.domain.external.DataPlatform;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HangHaeDataPlatform implements DataPlatform {
    @Override
    public void sendPaymentData(Long orderId, Long usePoint, Long discountPoint, LocalDateTime paidAt) {

    }
}
