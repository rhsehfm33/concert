package ms.parade.application.point;

import ms.parade.domain.point.PointHistory;
import ms.parade.domain.point.UserPoint;

public record UserPointResult(
    UserPoint userPoint,
    PointHistory pointHistory
) {
}
