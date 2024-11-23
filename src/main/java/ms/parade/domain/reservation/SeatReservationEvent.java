package ms.parade.domain.reservation;

import java.time.LocalDateTime;


public record SeatReservationEvent (
    long reservationId,
    long seatId,
    long userId,
    LocalDateTime reservationTime
) {
}
