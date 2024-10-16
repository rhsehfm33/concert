package ms.parade.application.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.reservation.SeatReservationInfo;
import ms.parade.domain.reservation.SeatReservationService;
import ms.parade.domain.seat.SeatInfo;
import ms.parade.domain.seat.SeatService;
import ms.parade.domain.seat.SeatStatus;

@Service
@RequiredArgsConstructor
public class ReservationFacade {
    private final SeatService seatService;
    private final SeatReservationService seatReservationService;

    @Transactional
    public SeatReservationResult reserveSeat(long userId, long seatId) {
        SeatInfo seatInfo = seatService.findByIdWithPessimisticLock(seatId).orElseThrow(
            () -> new IllegalArgumentException("Seat id[" + seatId + "]는 존재하지 않습니다.")
        );
        if (SeatStatus.BOOKED.equals(seatInfo.status())) {
            throw new IllegalArgumentException("Seat id[" + seatId + "]는 이미 예약됐습니다.");
        }

        seatInfo = seatService.updateStatus(seatId, SeatStatus.BOOKED);
        SeatReservationInfo seatReservationInfo = seatReservationService.createReservation(userId, seatId);
        return new SeatReservationResult(seatInfo, seatReservationInfo);
    }
}
