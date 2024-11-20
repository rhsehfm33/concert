package ms.parade.application.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.outbox.OutboxEventWrapper;
import ms.parade.domain.reservation.SeatReservationEvent;
import ms.parade.infrastructure.reservation.ReservationKafkaConfig;

@Service
@RequiredArgsConstructor
public class ReservationEventKafkaProducer implements ReservationEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(ReservationEventKafkaProducer.class);
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceEvent(OutboxEventWrapper<SeatReservationEvent> outboxEventWrapper) throws JsonProcessingException {
        long outboxId = outboxEventWrapper.outboxId();
        SeatReservationEvent seatReservationEvent = outboxEventWrapper.event();
        logger.info(
            "Send reservation data\n"
                + "outbox ID: {}, reservation ID: {}, seat ID: {}, user ID: {}, reservation time: {}",
            outboxId,
            seatReservationEvent.reservationId(),
            seatReservationEvent.seatId(),
            seatReservationEvent.userId(),
            seatReservationEvent.reservationTime()
        );

        // Send message
        String message = objectMapper.writeValueAsString(outboxEventWrapper);
        kafkaTemplate.send(ReservationKafkaConfig.SEAT_RESERVATION_TOPIC, message);
    }
}
