package ms.parade.interfaces.seat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class SeatController {

    @GetMapping("/concerts/{concertId}/available-seats")
    ResponseEntity<AvailableSeatsResponse> getAvailableSeats(
        @PathVariable long concertId,
        @RequestParam String date
    ) {
        List<SeatResponse> seats = new ArrayList<>(List.of(
            new SeatResponse(200, "A1"),
            new SeatResponse(201, "A2")));
        AvailableSeatsResponse availableSeatsResponse = new AvailableSeatsResponse(
            concertId,
            date,
            seats
        );
        return ResponseEntity.ok(availableSeatsResponse);
    }
}
