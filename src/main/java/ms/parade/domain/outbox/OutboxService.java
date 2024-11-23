package ms.parade.domain.outbox;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ms.parade.infrastructure.outbox.OutboxModel;
import ms.parade.infrastructure.outbox.OutboxParams;
import ms.parade.infrastructure.outbox.OutboxStatus;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Object extractEvent(String eventType, String eventData)
        throws JsonProcessingException, ClassNotFoundException {
        Class<?> eventClass = Class.forName(eventType);
        return objectMapper.readValue(eventData, eventClass);
    }

    @Transactional
    public OutboxInfo createOutbox(OutboxCommand outboxCommand) throws JsonProcessingException {
        String eventTopic = outboxCommand.eventTopic();
        String key = outboxCommand.eventKey();
        String eventType = outboxCommand.eventData().getClass().getName();
        String eventData = objectMapper.writeValueAsString(outboxCommand.eventData()); // 이벤트를 JSON으로 직렬화하는 메서드
        OutboxParams outboxParams = new OutboxParams(eventTopic, key, eventType, eventData);
        OutboxModel outboxModel = outboxRepository.createOutbox(outboxParams);
        return new OutboxInfo(outboxModel);
    }

    @Transactional
    public OutboxInfo changeStatus(long outboxId, OutboxStatus status) {
        OutboxModel outboxModel = outboxRepository.updateStatus(outboxId, status);
        return new OutboxInfo(outboxModel);
    }

    @Transactional
    public void processPendingOutboxEvents() throws JsonProcessingException, ClassNotFoundException {
        List<OutboxModel> outboxModels = outboxRepository.findAllByStatus(OutboxStatus.INIT);

        for (OutboxModel outboxModel : outboxModels) {
            Object event = extractEvent(outboxModel.eventType(), outboxModel.eventData());
            if (outboxModel.createdAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
                eventPublisher.publishEvent(event);
            }
        }
    }
}
