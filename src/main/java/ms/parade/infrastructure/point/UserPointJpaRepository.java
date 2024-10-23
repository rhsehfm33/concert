package ms.parade.infrastructure.point;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, Long> {
    @Modifying
    @Query("UPDATE UserPointEntity up"
        + " SET up.point = up.point + :amount"
        + " WHERE up.userId = :userId"
        + " AND up.point + :amount >= 0")
    int addPoint(@Param("userId") long userId, @Param("amount") long amount);

    Optional<UserPointEntity> findByUserId(long userId);
}
