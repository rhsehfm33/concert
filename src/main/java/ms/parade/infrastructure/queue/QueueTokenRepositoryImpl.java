package ms.parade.infrastructure.queue;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenRepository;
import ms.parade.domain.queue.QueueTokenStatus;

@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryImpl implements QueueTokenRepository {
    private final QueueTokenJpaRepository queueTokenJpaRepository;

    @Override
    public QueueToken save(QueueTokenParams queueTokenParams) {
        QueueTokenEntity queueTokenEntity = QueueTokenEntity.from(queueTokenParams);
        queueTokenEntity = queueTokenJpaRepository.save(queueTokenEntity);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public QueueToken updateStatus(long id, QueueTokenStatus queueTokenStatus) {
        QueueTokenEntity queueTokenEntity = queueTokenJpaRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("No such queue token")
        );
        queueTokenEntity.setStatus(queueTokenStatus);
        queueTokenJpaRepository.save(queueTokenEntity);
        return QueueTokenEntity.to(queueTokenEntity);
    }

    @Override
    public Optional<QueueToken> findById(long id) {
        Optional<QueueTokenEntity> queueTokenEntity = queueTokenJpaRepository.findById(id);
        return queueTokenEntity.map(QueueTokenEntity::to);
    }

    @Override
    public Optional<QueueToken> findByUserId(long userId) {
        Optional<QueueTokenEntity> queueTokenEntity = queueTokenJpaRepository.findByUserId(userId);
        return queueTokenEntity.map(QueueTokenEntity::to);
    }

    @Override
    public void deleteById(long id) {
        queueTokenJpaRepository.deleteById(id);
    }

}
