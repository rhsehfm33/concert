package ms.parade.infrastructure.queue;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.parade.domain.queue.QueueTokenStatus;

@Entity
@Getter
@Table(name = "queue_tokens")
@NoArgsConstructor
public class QueueTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    LocalDateTime expireTime;

    QueueTokenStatus queueTokenStatus;
}
