package kr.hhplus.be.server.payment.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.common.domain.service.ClockHolder;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.external.DataPlatform;
import kr.hhplus.be.server.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClockHolder clockHolder;
    private final DataPlatform dataPlatform;

    public Payment pay(Long orderId, Point usePoint, Point totalDiscountPoint) {
        Payment payment = new Payment(
                usePoint,
                totalDiscountPoint,
                orderId,
                clockHolder
        );
        Payment saved = paymentRepository.save(payment);

        dataPlatform.sendPaymentData(
                payment.getOrderId(),
                payment.getAmount().amount(),
                payment.getDiscountAmount().amount(),
                payment.getPaidAt()
        );

        return saved;
    }
}
