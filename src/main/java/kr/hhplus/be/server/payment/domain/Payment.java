package kr.hhplus.be.server.payment.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.common.domain.service.DateTimeHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "PAYMENT")
@Entity
public class Payment extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "amount"))
    private Point amount;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "discount_amount"))
    private Point discountAmount;
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    public Payment(Point amount, Point discountAmount, Long orderId, DateTimeHolder dateTimeHolder) {
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.orderId = orderId;
        this.paidAt = dateTimeHolder.now();
    }

}
