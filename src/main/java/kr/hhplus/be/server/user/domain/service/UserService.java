package kr.hhplus.be.server.user.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.Wallet;
import kr.hhplus.be.server.user.domain.WalletHistory;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.domain.service.dto.request.UsePointCommand;
import kr.hhplus.be.server.user.domain.service.dto.response.CheckPointDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public void chargePoint(UsePointCommand command) {
        User user = command.user();
        Integer amount = command.amount();
        WalletHistory walletHistory = user.chargePoint(amount);
        userRepository.save(walletHistory);
    }

    public void usePoint(UsePointCommand command) {
        User user = command.user();
        Integer amount = command.amount();
        WalletHistory walletHistory = user.deductPoint(amount);
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
