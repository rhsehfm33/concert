package ms.parade.application.queue;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueTokenService;
import ms.parade.domain.user.UserService;

@Service
@RequiredArgsConstructor
public class QueueTokenFacade {
    private final QueueTokenService queueTokenService;
    private final UserService userService;

    @Transactional
    public QueueResult putUnique(long userId) {
        userService.findById(userId).orElseThrow(
            () -> new EntityNotFoundException("SEAT_NOT_EXIST; 존재하지 않는 좌석입니다.")
        );
        return new QueueResult(queueTokenService.putUnique(userId));
    }

    @Transactional
    public QueueResult getById(long userId) {
        return new QueueResult(queueTokenService.getById(userId));
    }
}
