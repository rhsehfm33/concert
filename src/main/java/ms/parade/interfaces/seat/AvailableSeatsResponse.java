package ms.parade.interfaces.seat;

import java.util.List;

public record AvailableSeatsResponse(
    long concertId,
    String date,
    List<SeatResponse> availableSeats
) {
}
