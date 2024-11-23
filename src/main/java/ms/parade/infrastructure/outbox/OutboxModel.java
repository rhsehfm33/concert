package ms.parade.infrastructure.outbox;

import java.time.LocalDateTime;

// infra -> domain 의존성을 끊고자 사용해봄
public record OutboxModel(
    long id,
    String eventTopic,
    String eventKey,
    String eventType,
    String eventData,
    LocalDateTime createdAt,
    OutboxStatus outboxStatus
) {
}
