package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.BaseEntity;
import kr.hhplus.be.server.common.domain.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "WALLET_HISTORY")
@Entity
public class WalletHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Integer amount;
    @JoinColumn(name = "WALLET_ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Wallet wallet;

    public WalletHistory(Wallet wallet, TransactionType transactionType, Integer amount) {
        this.wallet = wallet;
        this.amount = transactionType.getSign() * amount;
    }

    public WalletHistory(Wallet wallet, TransactionType transactionType, Point point) {
        this(wallet,
                transactionType,
                point.amount()
                        .intValue()
        );
    }

    public WalletHistory(Wallet wallet, int amount) {
        this.wallet = wallet;
        this.amount = amount;
    }

    public WalletHistory(Long id, Wallet wallet, Integer amount) {
        this.id = id;
        this.wallet = wallet;
        this.amount = amount;
    }

    public WalletHistory(Long id, WalletHistory history) {
        this.id = id;
        this.wallet = history.wallet;
        this.amount = history.amount;
    }

    public Boolean isSaved() {
        return !(id == null || id == 0);
    }

    public Long getWalletId() {
        return wallet.getId();
    }
}
