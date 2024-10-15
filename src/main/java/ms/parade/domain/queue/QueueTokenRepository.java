package ms.parade.domain.queue;

import java.util.Optional;

import ms.parade.infrastructure.queue.QueueTokenParams;

public interface QueueTokenRepository {
    QueueToken save(QueueTokenParams queueTokenParams);
    QueueToken updateStatus(long id, QueueTokenStatus queueTokenStatus);
    Optional<QueueToken> findById(long id);
    Optional<QueueToken> findByUserId(long userId);
    void deleteById(long id);
}
