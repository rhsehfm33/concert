package ms.parade.infrastructure.queue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenRepository;
import ms.parade.domain.queue.QueueTokenStatus;

@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryImpl implements QueueTokenRepository {
    private final QueueTokenRedisRepository queueTokenRedisRepository;

    @Override
    public QueueToken save(QueueTokenParams queueTokenParams) {
        Instant now = Instant.now();

        long epochSecond = now.getEpochSecond();
        int nanoAdjustment = now.getNano();
        long totalNanoseconds = epochSecond * 1_000_000_000L + nanoAdjustment;

        QueueTokenEntity queueTokenEntity = QueueTokenEntity.from(totalNanoseconds, queueTokenParams);
        queueTokenEntity = queueTokenRedisRepository.save(queueTokenEntity);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public QueueToken updateStatus(long id, QueueTokenStatus queueTokenStatus) {
        QueueTokenEntity queueTokenEntity = queueTokenRedisRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 토큰입니다.")
        );
        queueTokenEntity.setStatus(queueTokenStatus);
        queueTokenEntity.setUpdatedAt(LocalDateTime.now());
        queueTokenRedisRepository.save(queueTokenEntity);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public int getWaitOrderByTime(LocalDateTime time) {
        return (int) queueTokenRedisRepository.findAll().stream()
            .filter(token -> token.getStatus() == QueueTokenStatus.WAIT && token.getCreatedAt().isBefore(time))
            .count();
    }

    @Override
    public Optional<QueueToken> findById(long id) {
        Optional<QueueTokenEntity> queueTokenEntity = queueTokenRedisRepository.findById(id);
        return queueTokenEntity.map(QueueTokenEntity::to);
    }

    @Override
    public Optional<QueueToken> findByUserId(long userId) {
        return queueTokenRedisRepository.findAll().stream()
            .filter(token -> token.getUserId() == userId)
            .findFirst()
            .map(QueueTokenEntity::to);
    }

    @Override
    public void deleteById(long id) {
        queueTokenRedisRepository.deleteById(id);
    }

    @Override
    public List<QueueToken> findFrontWaits() {
        return queueTokenRedisRepository.findAll().stream()
            .filter(token -> token.getStatus() == QueueTokenStatus.WAIT)
            .sorted(Comparator.comparingLong(QueueTokenEntity::getId))
            .limit(30)
            .map(QueueTokenEntity::to)
            .toList();
    }


    @Override
    public List<QueueToken> findTimeoutWaits() {
        return queueTokenRedisRepository.findAll().stream()
            .filter(token -> token.getStatus() == QueueTokenStatus.PASS
                && token.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(20)))
            .map(QueueTokenEntity::to)
            .toList();
    }
}
