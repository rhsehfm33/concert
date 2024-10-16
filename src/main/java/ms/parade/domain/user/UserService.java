package ms.parade.domain.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.point.PointType;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User updatePoint(long userId, long amount, PointType pointType) {
        if (PointType.CHARGE.equals(pointType)) {
            return userRepository.addPoint(userId, amount);
        } else if (PointType.SPEND.equals(pointType)) {
            return userRepository.addPoint(userId, -amount);
        } else {
            throw new IllegalArgumentException("잘못된 충전 타입입니다.");
        }
    }

    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }
}
