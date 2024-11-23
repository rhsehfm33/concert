package ms.parade.integration.application.payment;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ms.parade.application.payment.PaymentFacade;
import ms.parade.domain.outbox.OutboxRepository;
import ms.parade.domain.outbox.OutboxService;
import ms.parade.domain.point.UserPointRepository;
import ms.parade.domain.reservation.ReservationStatus;
import ms.parade.domain.reservation.SeatReservationRepository;
import ms.parade.domain.seat.SeatRepository;
import ms.parade.domain.seat.SeatStatus;
import ms.parade.infrastructure.point.UserPointParams;
import ms.parade.infrastructure.reservation.SeatReservationParams;
import ms.parade.infrastructure.seat.SeatParams;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PaymentFacadeWholeTest {
    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private OutboxService outboxService;

    @Test
    public void payForSeat_TransactionSuccess_PublishEvent() {
        // 더미 데이터 세팅
        long userId = 1;
        long seatId = 1;
        long reservationId = 1;
        seatReservationRepository.save(
            new SeatReservationParams(userId, seatId, ReservationStatus.PAYING, LocalDateTime.now())
        );
        seatRepository.save(new SeatParams(1, "A1", 10000, SeatStatus.BOOKED));
        userPointRepository.save(new UserPointParams(userId, reservationId));

        // 테스트 함수 실행
        paymentFacade.payForSeat(userId, seatId);

        // 리스너가 동작을 수행했는지 확인
        Assertions.assertTrue(outboxRepository.findById(1).isPresent());
    }
}
