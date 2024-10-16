package ms.parade.infrastructure.reservation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;

public interface SeatReservationJpaRepository extends JpaRepository<SeatReservationEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sr FROM SeatReservationEntity sr WHERE sr.id = :id")
    Optional<SeatReservationEntity> findByIdForUpdate(long id);
}
