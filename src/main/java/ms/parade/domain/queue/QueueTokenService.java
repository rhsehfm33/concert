package ms.parade.domain.queue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.infrastructure.queue.QueueTokenParams;

@Service
@Transactional
@RequiredArgsConstructor
public class QueueTokenService {
    private final QueueTokenRepository queueTokenRepository;

    public QueueTokenInfo putUnique(long userId) {
        final Optional<QueueToken> queueTokenOptional = queueTokenRepository.findByUserId(userId);
        if (queueTokenOptional.isPresent()) {
            throw new IllegalArgumentException("Token already exists");
        }

        QueueTokenParams queueTokenParams = new QueueTokenParams(
            userId,
            LocalDateTime.now(),
            null,
            QueueTokenStatus.WAIT
        );
        QueueToken queueToken = queueTokenRepository.save(queueTokenParams);

        int order = queueTokenRepository.getWaitOrderByTime(queueToken.createdAt());
        return new QueueTokenInfo(queueToken, order + 1);
    }

    public QueueTokenInfo findById(long uuid) {
        QueueToken queueToken = queueTokenRepository.findById(uuid).orElseThrow(
            () -> new IllegalArgumentException("No token found for uuid: " + uuid)
        );
        int order = queueTokenRepository.getWaitOrderByTime(queueToken.createdAt());
        return new QueueTokenInfo(queueToken, order + 1);
    }

    public QueueTokenInfo update(long uuid, QueueTokenStatus queueTokenStatus) {
        QueueToken queueToken = queueTokenRepository.updateStatus(uuid, queueTokenStatus);
        return new QueueTokenInfo(queueToken, 0);
    }

    public void deleteById(long id) {
        queueTokenRepository.deleteById(id);
    }
}
