package kr.hhplus.be.server.payment.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.service.DateTimeHolder;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.external.DataPlatform;
import kr.hhplus.be.server.payment.domain.repository.PaymentRepository;
import kr.hhplus.be.server.payment.domain.service.dto.request.PayCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DateTimeHolder dateTimeHolder;
    private final DataPlatform dataPlatform;

    public void pay(PayCommand command) {

        Long orderId = command.order().getId();
        Payment payment = new Payment(
                command.usePoint(),
                command.discountPoint(),
                orderId,
                dateTimeHolder);
        paymentRepository.save(payment);

        // 외부 Data Platform 전송
        dataPlatform.sendPaymentData(
                payment.getOrderId(),
                payment.getAmount().toLong(),
                payment.getDiscountAmount().toLong(),
                payment.getPaidAt()
        );
    }
}
