package ms.parade.domain.seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findByScheduleIdAndSeatStatus(long scheduleId, SeatStatus seatStatus);
    Optional<Seat> findByIdWithPessimisticLock(long id);
    Seat updateStatus(long id, SeatStatus seatStatus);
}
