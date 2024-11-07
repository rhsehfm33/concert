package ms.parade.interfaces.queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.application.queue.QueueResult;
import ms.parade.application.queue.QueueTokenFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/queue-tokens")
public class QueueTokenController {
    private final QueueTokenFacade queueTokenFacade;

    @GetMapping("/{uuid}")
    ResponseEntity<QueueTokenResponse> getQueueToken(@PathVariable String uuid) {
        QueueResult queueResult = queueTokenFacade.getById(uuid);
        QueueTokenResponse queueTokenResponse = new QueueTokenResponse(queueResult);
        return ResponseEntity.ok(queueTokenResponse);
    }

    @PostMapping("/{userId}")
    ResponseEntity<QueueTokenResponse> putQueueToken(@PathVariable long userId) {
        QueueResult queueResult = queueTokenFacade.putUnique(userId);
        QueueTokenResponse queueTokenResponse = new QueueTokenResponse(queueResult);
        return ResponseEntity.ok(queueTokenResponse);
    }

}
