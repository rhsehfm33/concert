package ms.parade.infrastructure.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatReservationJpaRepository extends JpaRepository<SeatReservationEntity, Long> {

}
