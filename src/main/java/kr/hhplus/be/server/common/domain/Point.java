package kr.hhplus.be.server.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Point {

    public static final Point ZERO = new Point(0L);
    @Column(insertable = false, updatable = false)
    private final Long amount;

    public Point(Integer amount) {
        this(
                amount.longValue()
        );
    }

    public Point(
            Long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.amount = amount;
    }

    public Point() {
        this(0L);
    }

    @Column(insertable = false, updatable = false)
    public Long amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(amount, point.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    public Point add(Point point) {
        return new Point(amount + point.amount);
    }

    public Point add(Integer amount) {
        return new Point(this.amount + amount);
    }

    public Point subtract(Point point) {
        return new Point(amount - point.amount);
    }

    public Point multiply(int multiplier) {
        return new Point(amount * multiplier);
    }

    public boolean isGreaterThan(Point maximumBalance) {
        return amount > maximumBalance.amount;
    }

    public Integer toInt() {
        return amount.intValue();
    }
}
