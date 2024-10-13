package ms.parade.interfaces.queue;

import ms.parade.domain.reservation.QueueStatus;

public record QueueResponse(
    String uuid,
    long userId,
    long order,
    long concertId,
    String concertTitle,
    long seatId,
    String seatName,
    String date,
    QueueStatus status,
    String detail) {
}
