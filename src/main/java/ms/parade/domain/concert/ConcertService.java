package ms.parade.domain.concert;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public List<ConcertScheduleInfo> findConcertSchedulesByConcertId(long concertId) {
        List<ConcertSchedule> concertSchedules = concertRepository.findSchedulesByConcertId(concertId);
        return concertSchedules.stream().map(ConcertScheduleInfo::new).toList();
    }
}
