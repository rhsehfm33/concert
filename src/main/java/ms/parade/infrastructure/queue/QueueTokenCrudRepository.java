package ms.parade.infrastructure.queue;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

public interface QueueTokenCrudRepository extends CrudRepository<QueueTokenEntity, String> {
    @NotNull
    List<QueueTokenEntity> findAll();
}