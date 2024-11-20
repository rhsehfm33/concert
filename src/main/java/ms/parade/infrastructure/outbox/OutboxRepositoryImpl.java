package ms.parade.infrastructure.outbox;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.outbox.OutboxRepository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {
    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public OutboxModel createOutbox(OutboxParams outboxParams) {
        OutboxEntity outboxEntity = outboxJpaRepository.save(
            new OutboxEntity(outboxParams.topic(), outboxParams.key(), outboxParams.eventType(), outboxParams.eventData())
        );
        return outboxEntity.toModel();
    }

    @Override
    public List<OutboxModel> findAllByStatus(OutboxStatus outboxStatus) {
        return outboxJpaRepository.findAllByStatus(outboxStatus).stream()
            .map(OutboxEntity::toModel).toList();
    }

    @Override
    public OutboxModel updateStatus(long outboxId, OutboxStatus status) {
        OutboxEntity outboxEntity = outboxJpaRepository.findById(outboxId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 outbox ID 입니다.")
        );
        outboxEntity.setStatus(status);
        return outboxJpaRepository.save(outboxEntity).toModel();
    }

    @Override
    public Optional<OutboxModel> findById(long outboxId) {
        return outboxJpaRepository.findById(outboxId).map(OutboxEntity::toModel);
    }
}
