package kr.hhplus.be.server.user.infra.persistence.jpa;

import kr.hhplus.be.server.user.domain.Wallet;
import kr.hhplus.be.server.user.domain.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletHistoryJpaRepository extends JpaRepository<WalletHistory, Long> {
    List<WalletHistory> findAllByWallet(Wallet wallet);
}
