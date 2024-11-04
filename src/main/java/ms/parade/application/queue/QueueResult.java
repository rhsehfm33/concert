package ms.parade.application.queue;

import java.time.LocalDateTime;

import ms.parade.domain.queue.QueueTokenInfo;
import ms.parade.domain.queue.QueueTokenStatus;

public record QueueResult(
    long uuid,
    long userId,
    QueueTokenStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    int order
) {
    public QueueResult(QueueTokenInfo queueTokenInfo) {
        this (
            queueTokenInfo.queueToken().uuid(),
            queueTokenInfo.queueToken().userId(),
            queueTokenInfo.queueToken().status(),
            queueTokenInfo.queueToken().createdAt(),
            queueTokenInfo.queueToken().updatedAt(),
            queueTokenInfo.order()
        );
    }
}
