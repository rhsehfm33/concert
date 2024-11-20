package ms.parade.domain.external;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = ReservationKafkaConfig.SEAT_RESERVATION_TOPIC, groupId = "seat-consumer-group1")
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException, ClassNotFoundException {
        OutboxEventWrapper<SeatReservationEvent> outboxEventWrapper = objectMapper.readValue(
            record.value(), OutboxEventWrapper.class
        );
        outboxService.changeStatus(outboxEventWrapper.outboxId(), OutboxStatus.RECEIVED);

        System.out.println("Key received: " + record.key());
        System.out.println("Message received: " + record.value());

        // Do its own logic

        // Success Outbox
        outboxService.changeStatus(outboxEventWrapper.outboxId(), OutboxStatus.SUCCESS);
    }
}