package ms.parade.domain.point;

import java.util.Optional;

import ms.parade.infrastructure.point.UserPointParams;

public interface UserPointRepository {
    int addPoint(long userId, long amount);
    UserPoint save(UserPointParams userPointParams);
    Optional<UserPoint> findByUserId(long id);
}
