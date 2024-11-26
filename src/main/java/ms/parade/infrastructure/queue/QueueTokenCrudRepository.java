package ms.parade.infrastructure.queue;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import lombok.NonNull;

public interface QueueTokenCrudRepository extends CrudRepository<QueueTokenEntity, String> {
    @NonNull
    List<QueueTokenEntity> findAll();
}
