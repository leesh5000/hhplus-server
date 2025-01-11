package kr.hhplus.be.server.user.domain.repository;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;
import kr.hhplus.be.server.user.domain.WalletHistory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findById(Long userId);
    User getById(Long userId) throws BusinessException;
    void save(WalletHistory walletHistory);
    List<WalletHistory> findAllWalletHistories(Wallet userWallet);
}
