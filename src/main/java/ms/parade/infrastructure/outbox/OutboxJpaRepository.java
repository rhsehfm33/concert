package ms.parade.infrastructure.outbox;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {
    List<OutboxEntity> findAllByStatus(OutboxStatus outboxStatus);
}
