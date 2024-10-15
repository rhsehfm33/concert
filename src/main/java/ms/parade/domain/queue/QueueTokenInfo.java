package ms.parade.domain.queue;

import java.time.LocalDateTime;

public record QueueTokenInfo(
    long uuid,
    long userId,
    QueueTokenStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    int order
) {
    public QueueTokenInfo(QueueToken queueToken, int order) {
        this(
            queueToken.uuid(),
            queueToken.userId(),
            queueToken.status(),
            queueToken.createdAt(),
            queueToken.updatedAt(),
            order
        );
    }
}
