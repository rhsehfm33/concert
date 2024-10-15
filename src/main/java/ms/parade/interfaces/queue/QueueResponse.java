package ms.parade.interfaces.queue;

import ms.parade.domain.queue.QueueTokenStatus;

public record QueueResponse(
    String uuid,
    long userId,
    long order,
    long concertId,
    String concertTitle,
    long seatId,
    String seatName,
    String date,
    QueueTokenStatus status,
    String detail) {
}
