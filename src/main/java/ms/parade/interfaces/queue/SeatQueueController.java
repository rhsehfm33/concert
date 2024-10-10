package ms.parade.interfaces.queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ms.parade.domain.reservation.QueueStatus;

@RestController
@RequestMapping("/v1")
public class SeatQueueController {

    @GetMapping("/queue/{uuid}")
    ResponseEntity<QueueResponse> getQueueToken(@PathVariable String uuid) {
        QueueResponse queueResponse = new QueueResponse(uuid, 1, 1, 1,
            "Billie Eilish 내한", 1, "A1", "2024-10-18",
            QueueStatus.WAITING, "Billie Eilish 내한 공연입니다.");
        return ResponseEntity.ok(queueResponse);
    }

}
