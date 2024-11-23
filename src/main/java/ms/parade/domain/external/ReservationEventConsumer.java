package ms.parade.domain.external;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.outbox.OutboxEventWrapper;
import ms.parade.domain.outbox.OutboxService;
import ms.parade.domain.reservation.SeatReservationEvent;
import ms.parade.infrastructure.outbox.OutboxStatus;
import ms.parade.infrastructure.reservation.ReservationKafkaConfig;

@Service
@RequiredArgsConstructor
public class ReservationEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ReservationEventConsumer.class);
    private static final String GROUP_ID = "seat-consumer-group1";

    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = ReservationKafkaConfig.SEAT_RESERVATION_TOPIC, groupId = GROUP_ID)
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        logger.info("Group:{}, Topic:{}, Partition:{}, Key:{} consumed",
            GROUP_ID, record.topic(), record.partition(), record.key());
        OutboxEventWrapper<SeatReservationEvent> outboxEventWrapper = objectMapper.readValue(
            record.value(), OutboxEventWrapper.class
        );
        outboxService.changeStatus(outboxEventWrapper.outboxId(), OutboxStatus.RECEIVED);

        // Do its own logic

        // Success Outbox
        outboxService.changeStatus(outboxEventWrapper.outboxId(), OutboxStatus.SUCCESS);
    }
}