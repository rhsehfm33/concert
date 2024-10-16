package ms.parade.application.reservation;

import ms.parade.domain.reservation.SeatReservationInfo;
import ms.parade.domain.seat.SeatInfo;

public record SeatReservationResult(
    SeatInfo seatInfo,
    SeatReservationInfo seatReservationInfo
) {
}
