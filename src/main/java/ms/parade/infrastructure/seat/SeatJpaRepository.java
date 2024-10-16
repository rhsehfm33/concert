package ms.parade.infrastructure.seat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import ms.parade.domain.seat.SeatStatus;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findByScheduleIdAndStatus(Long scheduleId, SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SeatEntity> findById(long id);
}
