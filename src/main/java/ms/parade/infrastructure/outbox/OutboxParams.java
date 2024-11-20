package ms.parade.infrastructure.outbox;

public record OutboxParams(
    String topic,
    String key,
    String eventType,
    String eventData
) {
}
