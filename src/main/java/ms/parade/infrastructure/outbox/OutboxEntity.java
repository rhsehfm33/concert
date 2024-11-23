package ms.parade.infrastructure.outbox;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "outboxes")
@NoArgsConstructor
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String eventType;

    @Setter
    private String eventData;

    private LocalDateTime createdAt;

    @Setter
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    public OutboxEntity(String eventType, String eventData) {
        this.eventType = eventType;
        this.eventData = eventData;
        this.createdAt = LocalDateTime.now();
        this.status = OutboxStatus.PENDING;
    }

    public OutboxModel toModel() {
        return new OutboxModel(id, eventType, eventData, createdAt, status);
    }
}
