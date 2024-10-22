package ms.parade.infrastructure.concert;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<ConcertSchedule> findScheduleByIdForUpdate(long id) {
        return concertScheduleJpaRepository.findByIdForUpdate(id).map(ConcertScheduleEntity::to);
    }

    @Override
    public int updateScheduleAvailableSeats(long id, int amount) {
        return concertScheduleJpaRepository.addAvailableSeats(id, amount);
    }

    @Override
    public ConcertSchedule saveSchedule(ConcertScheduleParams concertScheduleParams) {
        ConcertScheduleEntity concertScheduleEntity = ConcertScheduleEntity.from(concertScheduleParams);
        concertScheduleEntity = concertScheduleJpaRepository.save(concertScheduleEntity);
        return ConcertScheduleEntity.to(concertScheduleEntity);
    }
}
