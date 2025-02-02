package ms.parade.application.payment;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ms.parade.domain.payment.SeatPayment;
import ms.parade.domain.payment.SeatPaymentCommand;
import ms.parade.domain.payment.SeatPaymentService;
import ms.parade.domain.point.PointHistoryCommand;
import ms.parade.domain.point.PointHistoryService;
import ms.parade.domain.point.PointType;
import ms.parade.domain.point.UserPointService;
import ms.parade.domain.reservation.SeatReservation;
import ms.parade.domain.reservation.SeatReservationEvent;
import ms.parade.domain.reservation.SeatReservationService;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatService;
import ms.parade.domain.seat.SeatStatus;
import ms.parade.infrastructure.payment.SeatPaymentParams;
import ms.parade.infrastructure.point.PointHistoryParams;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentFacade {
    private final SeatReservationService seatReservationService;
    private final PointHistoryService pointHistoryService;
    private final SeatService seatService;
    private final UserPointService userPointService;
    private final SeatPaymentService seatPaymentService;
    private final ApplicationEventPublisher eventPublisher;

    public SeatPayment payForSeat(long userId, long reservationId) {
        // 예약 완료시키기
        SeatReservation seatReservation = seatReservationService.completeReservation(reservationId, userId);

        // 좌석 예약 상태인지 체크
        Seat seat = seatService.findByIdForUpdate(seatReservation.seatId()).orElseThrow(
            () -> new EntityNotFoundException("SEAT_NOT_EXIST; 존재하지 않는 좌석입니다.")
        );
        if (!seat.status().equals(SeatStatus.BOOKED)) {
            throw new IllegalArgumentException("NOT_RESERVED; 예약되지 않은 좌석입니다.");
        }

        // 포인트 차감
        userPointService.changeUserPoint(seatReservation.userId(), seat.price(), PointType.SPEND);

        // 결제 내역 저장
        SeatPaymentParams seatPaymentParams = new SeatPaymentParams(
            seatReservation.userId(),
            seatReservation.id(),
            "좌석 예약 건에 대해 결재했습니다."
        );
        SeatPayment seatPayment = seatPaymentService.createSeatPayment(new SeatPaymentCommand(seatPaymentParams));

        // 포인트 사용 내역 저장
        PointHistoryParams pointHistoryParams = new PointHistoryParams(
            seatReservation.userId(),
            PointType.SPEND,
            seat.price(),
            "좌석 예약 결제"
        );
        pointHistoryService.record(new PointHistoryCommand(pointHistoryParams));

        eventPublisher.publishEvent(new SeatReservationEvent(reservationId, seat.id(), userId, LocalDateTime.now()));

        // 결제 내역 반환
        return seatPayment;
    }
}
