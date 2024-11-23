package ms.parade.interfaces.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import ms.parade.application.reservation.ReservationDataProcessor;
import ms.parade.domain.outbox.OutboxService;
import ms.parade.domain.reservation.SeatReservationEvent;

@Service
@RequiredArgsConstructor
public class ReservationEventListener {
    private final OutboxService outboxService;
    private final ReservationDataProcessor reservationDataProcessor;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishOutReservationData(SeatReservationEvent seatReservationEvent) throws JsonProcessingException {
        try {
            reservationDataProcessor.processEvent(seatReservationEvent);
        } catch (Exception e) {
            outboxService.createOutbox(seatReservationEvent);
        }
    }
}
