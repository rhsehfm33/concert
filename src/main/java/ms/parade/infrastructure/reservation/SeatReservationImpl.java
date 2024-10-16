package ms.parade.infrastructure.reservation;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationRepository;

@Repository
@RequiredArgsConstructor
public class SeatReservationImpl implements SeatReservationRepository {
    private final SeatReservationJpaRepository seatReservationJpaRepository;

    @Override
    public SeatReservation save(SeatReservationParams seatReservationParams) {
        SeatReservationEntity seatReservationEntity = SeatReservationEntity.from(seatReservationParams);
        seatReservationEntity = seatReservationJpaRepository.save(seatReservationEntity);
        return SeatReservationEntity.to(seatReservationEntity);
    }
}
