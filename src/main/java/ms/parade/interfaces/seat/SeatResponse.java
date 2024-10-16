package ms.parade.interfaces.seat;

import ms.parade.domain.seat.SeatInfo;
import ms.parade.domain.seat.SeatStatus;

public record SeatResponse(
    long id,
    long scheduleId,
    String name,
    long price,
    SeatStatus status
) {
    public SeatResponse(SeatInfo seatInfo) {
        this(
            seatInfo.id(),
            seatInfo.scheduleId(),
            seatInfo.name(),
            seatInfo.price(),
            seatInfo.status()
        );
    }
}