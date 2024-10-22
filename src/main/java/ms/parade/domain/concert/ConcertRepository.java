package ms.parade.domain.concert;

import java.util.List;

import ms.parade.infrastructure.concert.ConcertScheduleParams;

public interface ConcertRepository {
    List<ConcertSchedule> findSchedulesByConcertId(long concertId);
    int updateScheduleAvailableSeats(long id, int availableSeats);
    ConcertSchedule saveSchedule(ConcertScheduleParams concertScheduleParams);
}
