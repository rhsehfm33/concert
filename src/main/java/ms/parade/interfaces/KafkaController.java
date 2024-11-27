package ms.parade.interfaces;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.reservation.SeatReservationEvent;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @PostMapping("/kafka-test")
    public void sendMessage() {
        SeatReservationEvent seatReservationEvent = new SeatReservationEvent(
            1, 1, 1, LocalDateTime.now()
        );
        eventPublisher.publishEvent(seatReservationEvent);
    }
}