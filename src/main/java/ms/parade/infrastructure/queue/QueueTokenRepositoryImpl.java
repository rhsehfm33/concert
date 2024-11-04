package ms.parade.infrastructure.queue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private static final String QUEUE_TOKEN_KEY = "queue_token_id";

    private final QueueTokenCrudRepository queueTokenCrudRepository;

    private final WaitingQueueTokenRepository waitingQueueTokenRepository;
    private final PassedQueueTokenRepository passedQueueTokenRepository;
    private final QueueTokenUserIdRepository queueTokenUserIdRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public QueueToken save(QueueTokenParams queueTokenParams) {
        long newId = redisTemplate.opsForValue().increment(QUEUE_TOKEN_KEY);
        QueueTokenEntity queueTokenEntity = QueueTokenEntity.from(newId, queueTokenParams);
        queueTokenEntity = queueTokenCrudRepository.save(queueTokenEntity);
        waitingQueueTokenRepository.addToSortedSet(newId);
        queueTokenUserIdRepository.addId(queueTokenEntity.getUserId(), newId);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public QueueToken updateAsPassed(long id) {
        QueueTokenEntity queueTokenEntity = queueTokenCrudRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 토큰입니다.")
        );
        queueTokenEntity.setStatus(QueueTokenStatus.PASS);
        queueTokenEntity.setUpdatedAt(LocalDateTime.now());
        queueTokenCrudRepository.save(queueTokenEntity);
        waitingQueueTokenRepository.removeFromSortedSet(id);
        passedQueueTokenRepository.addToSortedSet(id);

        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public int countCreatedAtBefore(long id) {
        return waitingQueueTokenRepository.countIdsLessThan(id);
    }

    @Override
    public Optional<QueueToken> findById(long id) {
        Optional<QueueTokenEntity> queueTokenEntity = queueTokenCrudRepository.findById(id);
        return queueTokenEntity.map(QueueTokenEntity::to);
    }

    @Override
    public Optional<QueueToken> findByUserId(long userId) {
        Long uuid = queueTokenUserIdRepository.getIdValue(userId);
        if (uuid == null) {
            return Optional.empty();
        }
        return queueTokenCrudRepository.findById(uuid).map(QueueTokenEntity::to);
    }

    @Override
    public void deleteById(long id) {
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
        Set<Long> latestIds = waitingQueueTokenRepository.getTopIds(30);
        return StreamSupport.stream(queueTokenCrudRepository.findAllById(latestIds).spliterator(), false)
            .map(QueueTokenEntity::to)
            .toList();
    }


    @Override
    public List<QueueToken> findTimeoutWaits() {
        Set<Long> latestIds = passedQueueTokenRepository.getTopIds(30);
        return StreamSupport.stream(queueTokenCrudRepository.findAllById(latestIds).spliterator(), false)
            .map(QueueTokenEntity::to)
            .toList();
    }
}
