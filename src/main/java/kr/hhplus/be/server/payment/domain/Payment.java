package kr.hhplus.be.server.payment.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.common.domain.service.ClockHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "payment")
@Entity
public class Payment extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @Embedded
    private Point amount;
    @Embedded
    private Point discountAmount;
    private Long orderId;
    private LocalDateTime paidAt;

    public Payment(Point amount, Point discountAmount, Long orderId, ClockHolder clockHolder) {
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.orderId = orderId;
        this.paidAt = clockHolder.now();
    }

}
