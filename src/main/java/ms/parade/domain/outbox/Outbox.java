package ms.parade.domain.outbox;

import java.time.LocalDateTime;

import ms.parade.infrastructure.outbox.OutboxModel;
import ms.parade.infrastructure.outbox.OutboxStatus;

public record Outbox(
    long id,
    String eventType,
    String eventData,
    LocalDateTime createdAt,
    OutboxStatus outboxStatus
) {
    public Outbox(OutboxModel outboxModel) {
        this(
            outboxModel.id(),
            outboxModel.eventType(),
            outboxModel.eventData(),
            outboxModel.createdAt(),
            outboxModel.outboxStatus()
        );
    }
}
