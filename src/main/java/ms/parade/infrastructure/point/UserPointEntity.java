package ms.parade.infrastructure.point;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.parade.domain.point.UserPoint;

@Entity
@Getter
@Table(name = "user_points")
@NoArgsConstructor
public class UserPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long point;

    LocalDateTime updatedAt;

    static UserPointEntity from(UserPointParams userPointParams) {
        UserPointEntity userPointEntity = new UserPointEntity();
        userPointEntity.userId = userPointParams.userId();
        userPointEntity.point = userPointParams.point();
        return userPointEntity;
    }

    static UserPoint to(UserPointEntity userPointEntity) {
        return new UserPoint(
            userPointEntity.id,
            userPointEntity.userId,
            userPointEntity.point
        );
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
