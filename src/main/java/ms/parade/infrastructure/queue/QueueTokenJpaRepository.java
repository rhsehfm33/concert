package ms.parade.infrastructure.queue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ms.parade.domain.queue.QueueTokenStatus;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, Long> {
    Optional<QueueTokenEntity> findByUserId(long userId);
    int countByStatusAndCreatedAtBefore(QueueTokenStatus status, LocalDateTime createdAt);
}
