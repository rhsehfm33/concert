package ms.parade.domain.queue;

import java.time.LocalDateTime;
import java.util.Optional;

import ms.parade.infrastructure.queue.QueueTokenParams;

public interface QueueTokenRepository {
    QueueToken save(QueueTokenParams queueTokenParams);
    QueueToken updateStatus(long id, QueueTokenStatus queueTokenStatus);
    int getWaitOrderByTime(LocalDateTime time);
    Optional<QueueToken> findById(long id);
    Optional<QueueToken> findByUserId(long userId);
    void deleteById(long id);
}
