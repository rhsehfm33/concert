package ms.parade.interfaces.queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueTokenService;

@Component
@RequiredArgsConstructor
public class QueueTokenScheduler {
    private final QueueTokenService queueTokenService;

    @Scheduled(fixedRate = 60000)
    public void schedulePassQueueTokens() {
        queueTokenService.passQueueTokens();
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleDeleteQueueTokens() {
        queueTokenService.deleteQueueTokens();
    }

}
