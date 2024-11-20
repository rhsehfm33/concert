package ms.parade.domain.outbox;

import java.time.LocalDateTime;

import ms.parade.infrastructure.outbox.OutboxModel;
import ms.parade.infrastructure.outbox.OutboxStatus;

public record OutboxInfo(
    long id,
    String eventTopic,
    String eventKey,
    String eventType,
    String eventData,
    LocalDateTime createdAt,
    OutboxStatus outboxStatus
) {
    public OutboxInfo(OutboxModel outboxModel) {
        this(
            outboxModel.id(),
            outboxModel.eventTopic(),
            outboxModel.eventKey(),
            outboxModel.eventType(),
            outboxModel.eventData(),
            outboxModel.createdAt(),
            outboxModel.outboxStatus()
        );
    }
}
