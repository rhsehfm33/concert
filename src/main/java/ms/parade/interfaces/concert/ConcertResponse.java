package ms.parade.interfaces.concert;

import java.util.List;

public record ConcertResponse(long concertId, List<String> availableDates) {
}
