package ms.parade.infrastructure.seat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.parade.domain.seat.SeatStatus;

@Entity
@Getter
@Table(name = "seats")
@NoArgsConstructor
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long concertId;

    private String name;

    private long price;

    private SeatStatus status;
}
