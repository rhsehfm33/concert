package ms.parade.application.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.point.PointHistory;
import ms.parade.domain.point.PointHistoryCommand;
import ms.parade.domain.point.PointHistoryService;
import ms.parade.domain.point.PointType;
import ms.parade.domain.point.UserPoint;
import ms.parade.domain.point.UserPointService;
import ms.parade.domain.user.UserService;
import ms.parade.infrastructure.point.PointHistoryParams;

@Service
@RequiredArgsConstructor
public class PointFacade {
    private final UserService userService;
    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;

    @Transactional
    public UserPointResult chargePoint(long userId, long amount) {
        userPointService.changeUserPoint(userId, amount, PointType.CHARGE);

        PointHistoryParams pointHistoryParams = new PointHistoryParams(
            userId,
            PointType.CHARGE,
            amount,
            "포인트 충전"
        );
        PointHistoryCommand pointHistoryCommand = new PointHistoryCommand(pointHistoryParams);
        PointHistory pointHistory = pointHistoryService.record(pointHistoryCommand);
        UserPoint userPoint = userPointService.getUserPoint(userId);

        return new UserPointResult(userPoint, pointHistory);
    }

    @Transactional
    public UserPoint getUserPoint(long userId) {
        return userPointService.getUserPoint(userId);
    }
}
