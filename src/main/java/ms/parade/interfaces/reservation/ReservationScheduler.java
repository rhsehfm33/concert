package ms.parade.interfaces.reservation;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ms.parade.application.reservation.ReservationFacade;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationFacade reservationFacade;

    @Scheduled(fixedRate = 60000)
    public void scheduleCancelTimeoutReservation() {
        reservationFacade.cancelTimeoutReservation();
    }
}
