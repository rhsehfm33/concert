package ms.parade.domain.reservation;

import java.util.Optional;

import ms.parade.infrastructure.reservation.SeatReservationParams;

public interface SeatReservationRepository {
    SeatReservation save(SeatReservationParams seatReservationParams);
    SeatReservation updateStatus(long seatId, ReservationStatus status);
    Optional<SeatReservation> findByIdForUpdate(long id);
}
