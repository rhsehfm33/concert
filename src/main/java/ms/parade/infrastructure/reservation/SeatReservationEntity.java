package ms.parade.infrastructure.reservation;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.parade.domain.reservation.ReservationStatus;

@Entity
@Getter
@Table(name = "seat_reservations")
@NoArgsConstructor
public class SeatReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long seatId;

    private Date date;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
