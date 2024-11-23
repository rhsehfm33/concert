package ms.parade.domain.outbox;

public record OutboxEventWrapper<T> (
    long outboxId,
    T event
) {
}