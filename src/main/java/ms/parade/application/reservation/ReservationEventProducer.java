package ms.parade.application.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;

import ms.parade.domain.outbox.OutboxEventWrapper;
import ms.parade.domain.reservation.SeatReservationEvent;

public interface ReservationEventProducer {
    void produceEvent(OutboxEventWrapper<SeatReservationEvent> seatReservationEvent) throws JsonProcessingException;
}
