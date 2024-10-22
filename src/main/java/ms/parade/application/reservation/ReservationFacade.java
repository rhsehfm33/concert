package ms.parade.application.reservation;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ms.parade.domain.concert.ConcertService;
import ms.parade.domain.reservation.ReservationStatus;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationService;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatService;
import ms.parade.domain.seat.SeatStatus;
import ms.parade.domain.user.UserService;

@Service
@RequiredArgsConstructor
public class ReservationFacade {
    private final UserService userService;
    private final SeatService seatService;
    private final ConcertService concertService;
    private final SeatReservationService seatReservationService;

    @Transactional
    public SeatReservationResult reserveSeat(long userId, long seatId) {
        Seat seat = seatService.findByIdForUpdate(seatId).orElseThrow(
            () -> new IllegalArgumentException("Seat id[" + seatId + "]는 존재하지 않습니다.")
        );
        userService.findById(userId).orElseThrow(
            () -> new EntityNotFoundException("사용자 정보가 잘못됐습니다.")
        );
        if (SeatStatus.BOOKED.equals(seat.status())) {
            throw new IllegalStateException("Seat id[" + seatId + "]는 이미 예약됐습니다.");
        }

        seat = seatService.updateStatus(seatId, SeatStatus.BOOKED);
        SeatReservation seatReservation = seatReservationService.create(userId, seatId);
        concertService.addAvailableSeats(seat.scheduleId(), -1);
        return new SeatReservationResult(seat, seatReservation);
    }

    @Transactional
    public void cancelTimeoutReservation() {
        List<SeatReservation> seatReservations = seatReservationService.findTimeoutReservationsForUpdate()
            .stream().sorted(Comparator.comparingLong(SeatReservation::seatId)).toList();

        List<Long> seatIds = seatReservations.stream().map(SeatReservation::seatId).toList();

        List<Seat> seats = seatService.findByIdsForUpdate(seatIds)
            .stream().sorted(Comparator.comparingLong(Seat::id)).toList();

        for (int i = 0; i < seats.size(); ++i) {
            seatReservationService.updateStatus(seatReservations.get(i).id(), ReservationStatus.CANCEL);
            seatService.updateStatus(seats.get(i).id(), SeatStatus.EMPTY);
            concertService.addAvailableSeats(seats.get(i).scheduleId(), 1);
        }
    }
}
