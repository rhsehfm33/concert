package ms.parade.application.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.reservation.SeatReservationEvent;

@Service
@RequiredArgsConstructor
public class ReservationDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ReservationDataProcessor.class);

    public void processEvent(SeatReservationEvent seatReservationEvent) {
        logger.info(
            "Send reservation data\n"
                + "reservation ID: {}, seat ID: {}, user ID: {}, reservation time: {}",
            seatReservationEvent.reservationId(),
            seatReservationEvent.seatId(),
            seatReservationEvent.userId(),
            seatReservationEvent.reservationTime()
        );
        // Send data here
    }
}
