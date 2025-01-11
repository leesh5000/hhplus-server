package kr.hhplus.be.server.user.infra.persistence;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;
import kr.hhplus.be.server.user.domain.WalletHistory;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.infra.persistence.jpa.UserJpaRepository;
import kr.hhplus.be.server.user.infra.persistence.jpa.WalletHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final WalletHistoryJpaRepository walletHistoryJpaRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public User getById(Long userId) throws BusinessException {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "ID가 %s인 사용자를 찾을 수 없습니다.".formatted(userId)
                ));
    }

    @Override
    public void save(WalletHistory history) {
        walletHistoryJpaRepository.save(history);
    }

    @Override
    public List<WalletHistory> findAllWalletHistories(Wallet userWallet) {
        return walletHistoryJpaRepository.findAllByWallet(userWallet);
    }

}
