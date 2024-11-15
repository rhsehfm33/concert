package ms.parade.domain.outbox;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ms.parade.infrastructure.outbox.OutboxModel;
import ms.parade.infrastructure.outbox.OutboxStatus;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private Object extractEvent(OutboxModel outboxModel)
        throws JsonProcessingException, ClassNotFoundException {
        String eventType = outboxModel.eventType();
        String eventData = outboxModel.eventData();
        Class<?> eventClass = null;
        eventClass = Class.forName(eventType);
        return objectMapper.readValue(eventData, eventClass);
    }

    @Transactional
    public Outbox createOutbox(Object event) throws JsonProcessingException {
        String eventType = event.getClass().getName();
        String eventData = objectMapper.writeValueAsString(event); // 이벤트를 JSON으로 직렬화하는 메서드
        OutboxModel outboxModel = outboxRepository.createOutbox(eventType, eventData);
        return new Outbox(outboxModel);
    }

    @Transactional
    public Outbox markAsProcessed(long outboxId) {
        OutboxModel outboxModel = outboxRepository.updateStatus(outboxId, OutboxStatus.PROCESSED);
        return new Outbox(outboxModel);
    }

    @Transactional
    public void processPendingOutboxEvents() throws JsonProcessingException, ClassNotFoundException {
        List<OutboxModel> outboxModels = outboxRepository.findAllByStatus(OutboxStatus.PENDING);

        for (OutboxModel outboxModel : outboxModels) {
            Object event = extractEvent(outboxModel);
            eventPublisher.publishEvent(event);
            markAsProcessed(outboxModel.id());
        }
    }
}
