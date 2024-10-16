package ms.parade.domain.point;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    public void record(PointHistoryCommand pointHistoryCommand) {
        pointHistoryRepository.save(pointHistoryCommand.pointHistoryParams());
    }
}
