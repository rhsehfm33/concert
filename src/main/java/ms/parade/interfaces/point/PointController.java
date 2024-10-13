package ms.parade.interfaces.point;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ms.parade.interfaces.common.MessageResponse;

@RestController
@RequestMapping("/v1")
public class PointController {

    @GetMapping("/users/{userId}/point")
    ResponseEntity<UserPointResponse> getUserPoint(@PathVariable long userId) {
        return ResponseEntity.ok(new UserPointResponse(userId, 1_000_000));
    }

    @PostMapping("/users/{userId}/point")
    ResponseEntity<MessageResponse> addUserPoint(
        @PathVariable long userId, @RequestBody UserPointRequest userPointRequest
    ) {
        return ResponseEntity.ok(new MessageResponse("성공적으로 포인트를 충전했습니다."));
    }

}
