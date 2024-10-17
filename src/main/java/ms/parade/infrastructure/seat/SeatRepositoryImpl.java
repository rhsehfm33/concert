package ms.parade.infrastructure.seat;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Seat> findByIdForUpdate(long id) {
        return seatJpaRepository.findByIdForUpdate(id).map(SeatEntity::to);
    }

    @Override
    public Seat updateStatus(long id, SeatStatus seatStatus) {
        SeatEntity seatEntity = seatJpaRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("No such seat exists!")
        );
        seatEntity.setStatus(SeatStatus.BOOKED);
        seatJpaRepository.save(seatEntity);
        return SeatEntity.to(seatEntity);
    }
}
