package ms.parade.infrastructure.reservation;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationKafkaConfig {
    public static final String SEAT_RESERVATION_TOPIC = "seat-reservation-topic";

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(SEAT_RESERVATION_TOPIC, 1, (short) 1); // 토픽명, 파티션 수, 복제본 수
    }
}
