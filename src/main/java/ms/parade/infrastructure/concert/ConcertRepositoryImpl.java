package ms.parade.infrastructure.concert;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.concert.ConcertRepository;
import ms.parade.domain.concert.ConcertSchedule;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Override
    public List<ConcertSchedule> findSchedulesByConcertId(long concertId) {
        List<ConcertScheduleEntity> concertScheduleEntities = concertScheduleJpaRepository.findByConcertId(concertId);

        return concertScheduleEntities.stream().map(ConcertScheduleEntity::to).toList();
    }
}
