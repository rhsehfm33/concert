package ms.parade.interfaces.point;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.application.point.PointFacade;
import ms.parade.application.point.UserPointResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PointController {
    private final PointFacade pointFacade;

    @GetMapping("/users/{userId}/point")
    ResponseEntity<UserPointResponse> getUserPoint(@PathVariable long userId) {
        long point = pointFacade.getUserPoint(userId);
        return ResponseEntity.ok(new UserPointResponse(userId, point));
    }

    @PostMapping("/users/{userId}/point")
    ResponseEntity<UserPointResponse> chargeUserPoint(
        @PathVariable long userId, @RequestBody UserPointRequest userPointRequest
    ) {
        UserPointResult userPointResult = pointFacade.chargePoint(
            userId, userPointRequest.amount()
        );
        return ResponseEntity.ok(new UserPointResponse(userId, userPointResult.user().point()));
    }
}
