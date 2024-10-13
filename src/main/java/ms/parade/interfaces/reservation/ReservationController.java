package ms.parade.interfaces.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ReservationController {

    @PostMapping("/seat-reservations")
    ResponseEntity<SeatReservationResponse> reserveSeat(
        @RequestBody SeatReservationRequest seatReservationRequest
    ) {
        return ResponseEntity.ok(new SeatReservationResponse("sdfsdfi1212"));
    }
}
