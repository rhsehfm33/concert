package ms.parade.infrastructure.outbox;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String eventTopic;

    private String eventKey;

    private String eventType;

    @Setter
    private String eventData;

    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    public OutboxEntity(String eventTopic, String eventKey, String eventType, String eventData) {
        this.eventTopic = eventTopic;
        this.eventKey = eventKey;
        this.eventType = eventType;
        this.eventData = eventData;
        this.createdAt = LocalDateTime.now();
        this.status = OutboxStatus.INIT;
    }

    public OutboxModel toModel() {
        return new OutboxModel(id, eventTopic, eventKey, eventType, eventData, createdAt, status);
    }
}
