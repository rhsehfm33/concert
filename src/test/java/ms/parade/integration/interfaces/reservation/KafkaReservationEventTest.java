package ms.parade.integration.interfaces.reservation;

import java.time.Duration;
import java.time.LocalDateTime;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import ms.parade.domain.outbox.OutboxRepository;
import ms.parade.domain.reservation.SeatReservationEvent;
import ms.parade.infrastructure.common.CustomTransactionManager;
import ms.parade.infrastructure.outbox.OutboxModel;
import ms.parade.infrastructure.outbox.OutboxStatus;
import ms.parade.infrastructure.reservation.ReservationKafkaConfig;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9093"
    },
    topics = {ReservationKafkaConfig.SEAT_RESERVATION_TOPIC}
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class KafkaReservationEventTest {
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private CustomTransactionManager transactionManager;

    @Test
    void publishEvent_ReservationEvent_OutboxSuccess() {
        triggerEvent();
        verifyOutboxCreated();
        verifyOutboxConsumed();
        verifyOutboxSuccess();
    }

    private void triggerEvent() {
        transactionManager.runInTransaction(() -> {
            SeatReservationEvent seatReservationEvent = new SeatReservationEvent(
                1L, 1L, 1L, LocalDateTime.now()
            );
            eventPublisher.publishEvent(seatReservationEvent);
            return null;
        });
    }

    private void verifyOutboxCreated() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(2))
            .untilAsserted(() -> Assertions.assertTrue(outboxRepository.findById(1L).isPresent()));
    }

    private void verifyOutboxConsumed() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(2))
            .untilAsserted(() -> {
                OutboxModel outboxModel = outboxRepository.findById(1L).orElse(null);
                Assertions.assertNotNull(outboxModel);
                Assertions.assertNotEquals(OutboxStatus.INIT, outboxModel.outboxStatus());
            });
    }

    private void verifyOutboxSuccess() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                OutboxModel outboxModel = outboxRepository.findById(1L).orElse(null);
                Assertions.assertNotNull(outboxModel);
                Assertions.assertEquals(OutboxStatus.SUCCESS, outboxModel.outboxStatus());
            });
    }
}
