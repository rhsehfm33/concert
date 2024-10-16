package ms.parade.infrastructure.seat;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatRepository;
import ms.parade.domain.seat.SeatStatus;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final SeatJpaRepository seatJpaRepository;

    @Override
    public List<Seat> findByScheduleIdAndSeatStatus(long scheduleId, SeatStatus seatStatus) {
        List<SeatEntity> seatEntities = seatJpaRepository.findByScheduleIdAndStatus(scheduleId, seatStatus);
        return seatEntities.stream().map(SeatEntity::to).toList();
    }
}
