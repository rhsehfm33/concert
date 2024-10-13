package ms.parade.interfaces.concert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ConcertController {

    @GetMapping("/concerts/{concertId}/available-dates")
    ResponseEntity<ConcertResponse> availableDates(@PathVariable long concertId) {
        ConcertResponse concertResponse = new ConcertResponse(1, new ArrayList<>(List.of("2024-10-11", "2024-10-12")));
        return ResponseEntity.ok(concertResponse);
    }

}
