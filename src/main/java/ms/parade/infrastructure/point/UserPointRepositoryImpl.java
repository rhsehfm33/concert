package ms.parade.infrastructure.point;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.point.UserPoint;
import ms.parade.domain.point.UserPointRepository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {
    private final UserPointJpaRepository userPointJpaRepository;

    @Override
    public int addPoint(long userId, long amount) {
        return userPointJpaRepository.addPoint(userId, amount);
    }

    @Override
    public UserPoint save(UserPointParams userPointParams) {
        UserPointEntity userPointEntity = UserPointEntity.from(userPointParams);
        userPointEntity = userPointJpaRepository.save(userPointEntity);
        return UserPointEntity.to(userPointEntity);
    }

    @Override
    public Optional<UserPoint> findByUserId(long userId) {
        return userPointJpaRepository.findByUserId(userId).map(UserPointEntity::to);
    }
}
