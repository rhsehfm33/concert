package ms.parade.interfaces.point;

import ms.parade.domain.point.PointType;

public record UserPointRequest(long amount, PointType type) {
}
