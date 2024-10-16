package ms.parade.interfaces.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.application.reservation.ReservationFacade;
import ms.parade.application.reservation.SeatReservationResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ReservationController {
    private final ReservationFacade reservationFacade;

    @PostMapping("/seat-reservations")
    ResponseEntity<SeatReservationResponse> reserveSeat(
        @RequestBody SeatReservationRequest seatReservationRequest
    ) {
        SeatReservationResult seatReservationResult = reservationFacade.reserveSeat(
            seatReservationRequest.userId(),
            seatReservationRequest.seatId()
        );

        return ResponseEntity.ok(new SeatReservationResponse(seatReservationResult));
    }
}
