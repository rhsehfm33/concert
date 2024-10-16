package ms.parade.domain.reservation;

import java.time.LocalDateTime;

public record SeatReservationInfo(
    long id,
    long userId,
    long seatId,
    ReservationStatus status,
    LocalDateTime createdAt
) {
    public SeatReservationInfo(SeatReservation seatReservation) {
        this (
            seatReservation.id(),
            seatReservation.userId(),
            seatReservation.seatId(),
            seatReservation.status(),
            seatReservation.createdAt()
        );
    }
}