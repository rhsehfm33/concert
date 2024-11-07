package ms.parade.domain.queue;

import java.util.List;
import java.util.Optional;

import ms.parade.infrastructure.queue.QueueTokenParams;

public interface QueueTokenRepository {
    QueueToken save(QueueTokenParams queueTokenParams);
    QueueToken updateAsPassed(String id);
    int countCreatedAtBefore(String id);
    Optional<QueueToken> findById(String id);
    Optional<QueueToken> findByUserId(long userId);
    void deleteById(String id);
    List<QueueToken> findFrontWaits();
    List<QueueToken> findTimeoutWaits();
}
