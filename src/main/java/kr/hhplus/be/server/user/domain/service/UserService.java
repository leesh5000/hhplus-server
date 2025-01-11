package kr.hhplus.be.server.user.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;
import kr.hhplus.be.server.user.domain.WalletHistory;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.domain.service.dto.request.UserPointCommand;
import kr.hhplus.be.server.user.domain.service.dto.response.CheckPointDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public void chargePoint(UserPointCommand command) {
        User user = userRepository.getById(
                command.userId()
        );
        WalletHistory walletHistory = user.chargePoint(
                command.amount()
        );
        userRepository.save(walletHistory);
    }

    public void usePoint(UserPointCommand command) {
        User user = userRepository.getById(
                command.userId()
        );

        WalletHistory walletHistory = user.deductPoint(
                command.amount()
        );
        userRepository.save(walletHistory);
    }

    public CheckPointDetail checkPoint(Long userId) {
        User user = userRepository.getById(userId);
        return CheckPointDetail.from(user);
    }

    public User getById(Long userId) {
        return userRepository.getById(userId);
    }

    public List<WalletHistory> getWalletHistories(Long userId) {
        User user = userRepository.getById(userId);
        Wallet userWallet = user.getWallet();
        return userRepository.findAllWalletHistories(userWallet);
    }
}
