package ms.parade.infrastructure.queue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenRepository;
import ms.parade.domain.queue.QueueTokenStatus;

@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryImpl implements QueueTokenRepository {
    private static final String QUEUE_TOKEN_KEY = "queue_tokens";
    private final QueueTokenCrudRepository queueTokenCrudRepository;

    private final RedisTemplate<String, Object> redisTemplate;
    private final WaitingQueueTokenRepository waitingQueueTokenRepository;
    private final PassedQueueTokenRepository passedQueueTokenRepository;
    private final QueueTokenUserIdRepository queueTokenUserIdRepository;

    @Override
    public QueueToken save(QueueTokenParams queueTokenParams) {
        String newId = UUID.randomUUID().toString();
        QueueTokenEntity queueTokenEntity = QueueTokenEntity.from(newId, queueTokenParams);
        queueTokenCrudRepository.save(queueTokenEntity);
        waitingQueueTokenRepository.addToSortedSet(newId, System.currentTimeMillis());
        queueTokenUserIdRepository.addId(queueTokenEntity.getUserId(), newId);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public QueueToken updateAsPassed(String uuid) {
        QueueTokenEntity queueTokenEntity = queueTokenCrudRepository.findById(uuid).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 토큰입니다.")
        );
        queueTokenEntity.setStatus(QueueTokenStatus.PASS);
        queueTokenEntity.setUpdatedAt(System.currentTimeMillis());
        queueTokenCrudRepository.save(queueTokenEntity);
        waitingQueueTokenRepository.removeFromSortedSet(uuid);
        passedQueueTokenRepository.addToSortedSet(uuid, System.currentTimeMillis());

        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public int countCreatedAtBefore(String id) {
        return waitingQueueTokenRepository.countIdsLessThan(id);
    }

    @Override
    public Optional<QueueToken> findById(String id) {
        return queueTokenCrudRepository.findById(id).map(QueueTokenEntity::to);
    }

    @Override
    public Optional<QueueToken> findByUserId(long userId) {
        String uuid = queueTokenUserIdRepository.getIdValue(userId);
        if (uuid == null) {
            return Optional.empty();
        }
        return queueTokenCrudRepository.findById(uuid).map(QueueTokenEntity::to);
    }

    @Override
    public void deleteById(String id) {
        Optional<QueueTokenEntity> queueTokenEntity = queueTokenCrudRepository.findById(id);
        if (queueTokenEntity.isPresent()) {
            queueTokenCrudRepository.deleteById(id);
            passedQueueTokenRepository.removeFromSortedSet(id);
            waitingQueueTokenRepository.removeFromSortedSet(id);
            queueTokenUserIdRepository.removeId(queueTokenEntity.get().getUserId());
        }
    }

    @Override
    public List<QueueToken> findFrontWaits() {
        Set<String> latestIds = waitingQueueTokenRepository.getTopIds(30);
        return StreamSupport.stream(queueTokenCrudRepository.findAllById(latestIds).spliterator(), false)
            .map(QueueTokenEntity::to)
            .toList();
    }


    @Override
    public List<QueueToken> findTimeoutWaits() {
        LocalDateTime now = LocalDateTime.now();
        Set<String> latestIds = passedQueueTokenRepository.getTopIds(30);
        return StreamSupport.stream(queueTokenCrudRepository.findAllById(latestIds).spliterator(), false)
            .filter(queueTokenEntity -> {
                    LocalDateTime updatedAt =  LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(queueTokenEntity.getUpdatedAt()), ZoneId.systemDefault()
                    );
                    return now.minusMinutes(20L).isBefore(updatedAt);
                }
            )
            .map(QueueTokenEntity::to)
            .toList();
    }
}
