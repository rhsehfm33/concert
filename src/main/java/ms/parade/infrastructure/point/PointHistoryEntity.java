package ms.parade.infrastructure.point;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.parade.domain.point.PointType;

@Entity
@Getter
@Table(name = "point_history")
@NoArgsConstructor
public class PointHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private PointType type;

    private long amount;

    private String reason;
}
