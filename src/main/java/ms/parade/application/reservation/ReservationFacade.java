package ms.parade.application.reservation;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ms.parade.domain.concert.ConcertService;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationService;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatService;
import ms.parade.domain.seat.SeatStatus;
import ms.parade.domain.user.UserService;
import ms.parade.infrastructure.common.CustomTransactionManager;
import ms.parade.infrastructure.common.RedisLockHandler;

@Service
@RequiredArgsConstructor
public class ReservationFacade {
    private final UserService userService;
    private final SeatService seatService;
    private final ConcertService concertService;
    private final SeatReservationService seatReservationService;
    private final RedisLockHandler redisLockHandler;
    private final CustomTransactionManager customTransactionManager;

    public SeatReservationResult reserveSeat(long userId, long seatId) {
        // 좌석 예약 함수
        Supplier<SeatReservationResult> reserveSeatFunction = () -> {
            Seat seat = seatService.findByIdForUpdate(seatId).orElseThrow(
                () -> new EntityNotFoundException("SEAT_NOT_EXIST; Seat id[" + seatId + "]는 존재하지 않습니다.")
            );
            userService.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("USER_NOT_FOUND; 해당 사용자는 존재하지 않습니다.")
            );
            if (SeatStatus.BOOKED.equals(seat.status())) {
                throw new IllegalStateException("SEAT_ALREADY_RESERVED; Seat id[" + seatId + "]는 이미 예약됐습니다.");
            }

            seat = seatService.updateStatus(seatId, SeatStatus.BOOKED);
            SeatReservation seatReservation = seatReservationService.create(userId, seatId);
            concertService.addAvailableSeats(seat.scheduleId(), -1);
            return new SeatReservationResult(seat, seatReservation);
        };

        return redisLockHandler.runOnLock("SEAT_ID:" + seatId, 10_000L, 10_000L,
            () -> customTransactionManager.runInTransaction(reserveSeatFunction));
    }

    @Transactional
    public void cancelTimeoutReservation() {
        List<SeatReservation> seatReservations = seatReservationService.findTimeoutReservationsForUpdate()
            .stream().sorted(Comparator.comparingLong(SeatReservation::seatId)).toList();

        List<Long> seatIds = seatReservations.stream().map(SeatReservation::seatId).toList();

        List<Seat> seats = seatService.findByIdsForUpdate(seatIds)
            .stream().sorted(Comparator.comparingLong(Seat::id)).toList();

        for (int i = 0; i < seats.size(); ++i) {
            seatReservationService.cancelReservation(seatReservations.get(i).id(), seatReservations.get(i).userId());
            seatService.updateStatus(seats.get(i).id(), SeatStatus.EMPTY);
            concertService.addAvailableSeats(seats.get(i).scheduleId(), 1);
        }
    }
}
