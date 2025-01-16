package kr.hhplus.be.server.payment.domain.repository;

import kr.hhplus.be.server.payment.domain.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {
    void save(Payment payment);
}
