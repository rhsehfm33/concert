package ms.parade.interfaces.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import ms.parade.application.reservation.ReservationEventProducer;
import ms.parade.domain.outbox.OutboxCommand;
import ms.parade.domain.outbox.OutboxEventWrapper;
import ms.parade.domain.outbox.OutboxInfo;
import ms.parade.domain.outbox.OutboxService;
import ms.parade.domain.reservation.SeatReservationEvent;
import ms.parade.infrastructure.reservation.ReservationKafkaConfig;

@Service
@RequiredArgsConstructor
public class ReservationEventListener {
    private final OutboxService outboxService;
    private final ReservationEventProducer reservationEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishOutReservationData(SeatReservationEvent seatReservationEvent) throws JsonProcessingException {
        // outbox 처리
        OutboxCommand outboxCommand = new OutboxCommand(
            ReservationKafkaConfig.SEAT_RESERVATION_TOPIC,
            Long.toString(seatReservationEvent.reservationId()),
            SeatReservationEvent.class.getName(),
            seatReservationEvent
        );
        OutboxInfo outboxInfo = outboxService.createOutbox(outboxCommand);
        OutboxEventWrapper<SeatReservationEvent> outboxEventWrapper = new OutboxEventWrapper<SeatReservationEvent>(
            outboxInfo.id(),
            seatReservationEvent
        );

        reservationEventProducer.produceEvent(outboxEventWrapper);
    }
}
