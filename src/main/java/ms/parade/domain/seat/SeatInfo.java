package ms.parade.domain.seat;

public record SeatInfo(
    long id,
    long scheduleId,
    String name,
    long price,
    SeatStatus status
) {
    public SeatInfo(Seat seat) {
        this(
            seat.id(),
            seat.scheduleId(),
            seat.name(),
            seat.price(),
            seat.status()
        );
    }
}