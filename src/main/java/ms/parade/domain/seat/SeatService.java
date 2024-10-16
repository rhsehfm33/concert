package ms.parade.domain.seat;

import java.util.List;

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
}
