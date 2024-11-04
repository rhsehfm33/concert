package ms.parade.infrastructure.queue;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenStatus;

@Getter
@RedisHash("queue_tokens")
@NoArgsConstructor
public class QueueTokenEntity {
    @Id
    private long id;

    private long userId;

    @Setter
    @Enumerated(EnumType.STRING)
    private QueueTokenStatus status;

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    public static QueueTokenEntity from(long id, QueueTokenParams queueTokenParams) {
        QueueTokenEntity tokenEntity = new QueueTokenEntity();
        tokenEntity.id = id;
        tokenEntity.userId = queueTokenParams.userId();
        tokenEntity.status = queueTokenParams.status();
        tokenEntity.createdAt = queueTokenParams.createdAt();
        tokenEntity.updatedAt = queueTokenParams.updatedAt();
        return tokenEntity;
    }

    public static QueueToken to(QueueTokenEntity queueTokenEntity) {
        return new QueueToken(
            queueTokenEntity.id,
            queueTokenEntity.userId,
            queueTokenEntity.status,
            queueTokenEntity.createdAt,
            queueTokenEntity.updatedAt
        );
    }
}
