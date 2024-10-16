package ms.parade.domain.seat;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public List<SeatInfo> findAvailableSeats(long scheduleId) {
        List<Seat> seats = seatRepository.findByScheduleIdAndSeatStatus(scheduleId, SeatStatus.EMPTY);
        return seats.stream().map(SeatInfo::new).toList();
    }

    public Optional<SeatInfo> findByIdWithPessimisticLock(long seatId) {
        return seatRepository.findByIdWithPessimisticLock(seatId).map(SeatInfo::new);
    }

    public SeatInfo reserveSeat(long seatId) {
        Seat seat = seatRepository.updateStatus(seatId, SeatStatus.BOOKED);
        return new SeatInfo(seat);
    }
}
