package ms.parade.infrastructure.queue;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, Long> {
    Optional<QueueTokenEntity> findByUserId(long userId);
}
