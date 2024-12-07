package ms.parade.interfaces.zdummy;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.application.queue.QueueResult;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenInfo;
import ms.parade.domain.queue.QueueTokenRepository;
import ms.parade.domain.queue.QueueTokenStatus;
import ms.parade.infrastructure.queue.QueueTokenCrudRepository;
import ms.parade.infrastructure.queue.QueueTokenParams;
import ms.parade.interfaces.queue.QueueTokenResponse;

@Profile({"default", "dev"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy/queue-tokens")
public class QueueTokenTestController {
    private final QueueTokenRepository queueTokenRepository;
    private final QueueTokenCrudRepository queueTokenCrudRepository;

    @DeleteMapping
    private void resetRedis() {
        queueTokenCrudRepository.deleteAll();
    }

    @PostMapping("/{userId}")
    ResponseEntity<QueueTokenResponse> putQueueToken(@PathVariable long userId) {
        final Optional<QueueToken> queueTokenOptional = queueTokenRepository.findByUserId(userId);
        if (queueTokenOptional.isPresent()) {
            throw new IllegalArgumentException("Token already exists");
        }

        QueueTokenParams queueTokenParams = new QueueTokenParams(
            userId,
            LocalDateTime.now(),
            null,
            QueueTokenStatus.PASS
        );
        QueueToken queueToken = queueTokenRepository.save(queueTokenParams);

        int order = queueTokenRepository.countCreatedAtBefore(queueToken.uuid());
        QueueTokenInfo queueTokenInfo = new QueueTokenInfo(queueToken, order + 1);
        QueueResult queueResult = new QueueResult(queueTokenInfo);
        return ResponseEntity.ok(new QueueTokenResponse(queueResult));
    }

}
