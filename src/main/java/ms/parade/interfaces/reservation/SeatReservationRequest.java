package ms.parade.interfaces.reservation;

public record SeatReservationRequest(
    long userId, long seatId
) {
}
