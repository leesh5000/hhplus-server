package kr.hhplus.be.server.mock.repository;

import kr.hhplus.be.server.common.domain.BusinessException;
import kr.hhplus.be.server.common.domain.ErrorCode;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;
import kr.hhplus.be.server.user.domain.WalletHistory;
import kr.hhplus.be.server.user.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong autoIncrementedId = new AtomicLong(0);
    private final List<User> userData = Collections.synchronizedList(
        new ArrayList<>(
            List.of()
        )
    );
    private final List<WalletHistory> walletHistoryData = Collections.synchronizedList(
        new ArrayList<>()
    );

    @Override
    public Optional<User> findById(Long userId) {
        return userData.stream()
                .filter(item -> item
                        .getId()
                        .equals(userId)
                )
                .findAny();
    }

    @Override
    public User getById(Long userId) throws BusinessException {
        return findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found"
                ));
    }

    @Override
    public void save(WalletHistory history) {
        if (!history.isSaved()) {
            WalletHistory newHistory = new WalletHistory(
                    autoIncrementedId.incrementAndGet(),
                    history);
            walletHistoryData.add(newHistory);
        } else {
            walletHistoryData.removeIf(item -> item.getId().equals(history.getId()));
            walletHistoryData.add(history);
        }
    }

    @Override
    public List<WalletHistory> findAllWalletHistories(Wallet userWallet) {
        return walletHistoryData.stream().filter(item -> item.getWallet().equals(userWallet)).toList();
    }

    public void save(User user) {
        userData.add(user);
    }
}
