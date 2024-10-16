package ms.parade.domain.concert;

import java.util.List;

public interface ConcertRepository {
    List<ConcertSchedule> findSchedulesByConcertId(long concertId);
}
