package ms.parade.infrastructure.concert;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
    List<ConcertScheduleEntity> findByConcertId(Long concertId);
}
