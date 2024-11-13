package ms.parade.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
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
            ReservationStatus.PAYING,
            LocalDateTime.now()
        );

        return seatReservationRepository.save(seatReservationParams);
    }

    public SeatReservation cancelReservation(long reservationId, long userId) {
        SeatReservation seatReservation = findByIdForUpdate(reservationId).orElseThrow(
            () -> new EntityNotFoundException("RESERVATION_NOT_EXIST; 존재하지 않는 예약입니다.")
        );
        if (seatReservation.userId() != userId) {
            throw new IllegalArgumentException("USER_NOT_MATCHING; 해당 예약에 대한 권한이 없습니다.");
        }
        return seatReservationRepository.updateStatus(reservationId, ReservationStatus.CANCEL);
    }

    public SeatReservation completeReservation(long reservationId, long userId) {
        // 예약 완료시키기
        SeatReservation seatReservation = findByIdForUpdate(reservationId).orElseThrow(
            () -> new EntityNotFoundException("RESERVATION_NOT_EXIST; 존재하지 않는 예약입니다.")
        );
        if (seatReservation.userId() != userId) {
            throw new IllegalArgumentException("USER_NOT_MATCHING; 해당 예약에 대한 권한이 없습니다.");
        }
        if (!ReservationStatus.PAYING.equals(seatReservation.status())) {
            throw new IllegalArgumentException("NOT_RESERVED; 결제할 수 없는 예약 건입니다.");
        }
        return seatReservationRepository.updateStatus(reservationId, ReservationStatus.COMPLETE);
    }

    public Optional<SeatReservation> findByIdForUpdate(long id) {
        return seatReservationRepository.findByIdForUpdate(id);
    }

    public List<SeatReservation> findTimeoutReservationsForUpdate() {
        return seatReservationRepository.findByStatusAndCreatedAtBeforeForUpdate(
            ReservationStatus.PAYING, LocalDateTime.now().minusMinutes(5)
        );
    }
}
