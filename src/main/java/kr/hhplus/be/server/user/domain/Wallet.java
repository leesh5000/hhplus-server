package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.common.domain.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "WALLET")
@Entity
public class Wallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @AttributeOverride(name = "amount", column = @Column(name = "balance"))
    private Point balance = new Point(0);

    private static final Point MINIMUM_BALANCE = new Point(0);
    private static final Point MAXIMUM_BALANCE = new Point(1_000_000_000_000_000L);

    public Wallet(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private void verifyMaximumBalanceExceed(Point point) {
        if (balance.add(point).isGreaterThan(MAXIMUM_BALANCE)) {
            throw new BusinessException(
                    ErrorCode.MAXIMUM_BALANCE_EXCEED,
                    "ID가 %d인 유저 지갑이 최대 한도 %d를 초과했습니다.".formatted(id, MAXIMUM_BALANCE.amount())
            );
        }
    }

    public WalletHistory deposit(Point point) {
        verifyMaximumBalanceExceed(point);
        balance = balance.add(point);
        return new WalletHistory(this, TransactionType.DEPOSIT, point);
    }

    public void deposit(Long point) {
        deposit(new Point(point));
    }

    public WalletHistory deposit(Integer amount) {
        Point point = new Point(amount);
        return deposit(point);
    }

    public WalletHistory withdraw(Integer amount) {
        Point subtractPoint = new Point(amount);
        balance = balance.subtract(subtractPoint);
        return new WalletHistory(this, TransactionType.WITHDRAW, amount);
    }

    public static Point getMaximumBalance() {
        return MAXIMUM_BALANCE;
    }

    public static Point getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}
