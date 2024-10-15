package ms.parade.interfaces.queue;

import java.time.LocalDateTime;

import ms.parade.domain.queue.QueueTokenInfo;
import ms.parade.domain.queue.QueueTokenStatus;

public record QueueTokenResponse(
    long uuid,
    long userId,
    QueueTokenStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    int order
) {
    public QueueTokenResponse(QueueTokenInfo queueTokenInfo) {
        this(
            queueTokenInfo.uuid(),
            queueTokenInfo.userId(),
            queueTokenInfo.status(),
            queueTokenInfo.createdAt(),
            queueTokenInfo.updatedAt(),
            queueTokenInfo.order()
        );
    }
}
