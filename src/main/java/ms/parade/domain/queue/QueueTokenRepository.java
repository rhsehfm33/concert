package ms.parade.domain.queue;

import java.util.List;
import java.util.Optional;

import ms.parade.infrastructure.queue.QueueTokenParams;

public interface QueueTokenRepository {
    QueueToken save(QueueTokenParams queueTokenParams);
    QueueToken updateAsPassed(long id);
    int countCreatedAtBefore(long id);
    Optional<QueueToken> findById(long id);
    Optional<QueueToken> findByUserId(long userId);
    void deleteById(long id);
    List<QueueToken> findFrontWaits();
    List<QueueToken> findTimeoutWaits();
}
