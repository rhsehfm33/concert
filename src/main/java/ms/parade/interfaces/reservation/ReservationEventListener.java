package ms.parade.interfaces.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ReservationEventListener.class);

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

        OutboxInfo outboxInfo = null;
        try {
            outboxInfo = outboxService.createOutbox(outboxCommand);
        } catch (JsonProcessingException e) {
            logger.error(" 좌석 예약[id:{}] outbox 저장에 실패했습니다.", seatReservationEvent.reservationId());
            logger.error(e.getMessage());
            throw e;
        }

        OutboxEventWrapper<SeatReservationEvent> outboxEventWrapper = new OutboxEventWrapper<>(
            outboxInfo.id(),
            seatReservationEvent
        );

        reservationEventProducer.produceEvent(outboxEventWrapper);
    }
}
