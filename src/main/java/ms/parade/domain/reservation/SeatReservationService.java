package ms.parade.domain.reservation;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.infrastructure.reservation.SeatReservationParams;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatReservationService {
    private final SeatReservationRepository seatReservationRepository;

    public SeatReservation create(long userId, long seatId) {
        SeatReservationParams seatReservationParams = new SeatReservationParams(
            userId,
            seatId,
            ReservationStatus.BOOKING,
            LocalDateTime.now()
        );

        return seatReservationRepository.save(seatReservationParams);
    }

    public SeatReservation updateStatus(long reservationId, ReservationStatus status) {
        return seatReservationRepository.updateStatus(reservationId, status);
    }
}
