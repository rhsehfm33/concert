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
        return ResponseEntity.ok(new UserPointResponse(userId, 1_000_000));
    }

    @PostMapping("/users/{userId}/point")
    ResponseEntity<UserPointResponse> changeUserPoint(
        @PathVariable long userId, @RequestBody UserPointRequest userPointRequest
    ) {
        UserPointResult userPointResult = pointFacade.changeUserPoint(userId, userPointRequest.amount(), userPointRequest.type());
        UserPointResponse userPointResponse = new UserPointResponse(userId, userPointResult.user().point());
        return ResponseEntity.ok(userPointResponse);
    }
}
