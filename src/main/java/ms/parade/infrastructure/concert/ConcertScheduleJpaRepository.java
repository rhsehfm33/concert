package ms.parade.infrastructure.concert;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
    List<ConcertScheduleEntity> findByConcertId(Long concertId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cs FROM ConcertScheduleEntity cs WHERE cs.id = :id")
    Optional<ConcertScheduleEntity> findByIdForUpdate(@Param("id") long id);

    @Modifying
    @Query("UPDATE ConcertScheduleEntity cs"
        + " SET cs.availableSeats = cs.availableSeats + :amount"
        + " WHERE cs.id = :id"
        + " AND cs.availableSeats + :amount <= cs.allSeats"
        + " AND cs.availableSeats + :amount >= 0")
    int addAvailableSeats(@Param("id") Long id, @Param("amount") long amount);

}
