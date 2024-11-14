package ms.parade.interfaces.outbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.outbox.OutboxService;

@Service
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxService outboxService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void processPendingOutboxEvents() throws JsonProcessingException, ClassNotFoundException {
        outboxService.processPendingOutboxEvents();
    }
}
