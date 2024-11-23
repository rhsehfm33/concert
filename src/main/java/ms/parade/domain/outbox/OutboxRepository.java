package ms.parade.domain.outbox;

import java.util.List;
import java.util.Optional;

import ms.parade.infrastructure.outbox.OutboxModel;
import ms.parade.infrastructure.outbox.OutboxStatus;

public interface OutboxRepository {
    OutboxModel createOutbox(String eventType, String eventData);

    List<OutboxModel> findAllByStatus(OutboxStatus outboxStatus);

    OutboxModel updateStatus(long outboxId, OutboxStatus status);

    Optional<OutboxModel> findById(long outboxId);
}
