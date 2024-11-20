package ms.parade.domain.outbox;

public record OutboxCommand(
    String eventTopic,
    String eventKey,
    String eventType,
    Object eventData
) {
}
