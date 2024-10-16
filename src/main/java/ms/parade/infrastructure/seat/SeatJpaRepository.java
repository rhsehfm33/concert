package ms.parade.infrastructure.seat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ms.parade.domain.seat.SeatStatus;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findByScheduleIdAndStatus(Long scheduleId, SeatStatus status);
}
