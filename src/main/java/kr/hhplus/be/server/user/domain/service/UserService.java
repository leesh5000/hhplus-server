package kr.hhplus.be.server.user.domain.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.domain.Point;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.WalletHistory;
import kr.hhplus.be.server.user.domain.repository.UserRepository;
import kr.hhplus.be.server.user.domain.service.dto.request.ChargePointCommand;
import kr.hhplus.be.server.user.domain.service.dto.response.CheckPointDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public void chargePoint(ChargePointCommand command) {

        Long userId = command.userId();
        Integer amount = command.amount();

        User user = this.getById(userId);
        WalletHistory walletHistory = user.chargePoint(amount);
        userRepository.save(walletHistory);
    }

    public void usePoint(User user, Point point) {
        WalletHistory walletHistory = user.deductPoint(point);
        userRepository.save(walletHistory);
    }

    public CheckPointDetail checkPoint(Long userId) {
        User user = userRepository.getById(userId);
        return CheckPointDetail.from(user);
    }

    public User getById(Long userId) {
        return userRepository.getById(userId);
    }
}
