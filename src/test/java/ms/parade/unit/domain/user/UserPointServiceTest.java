package ms.parade.unit.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ms.parade.domain.point.PointType;
import ms.parade.domain.point.UserPoint;
import ms.parade.domain.point.UserPointRepository;
import ms.parade.domain.point.UserPointService;

@ExtendWith(MockitoExtension.class)
public class UserPointServiceTest {
    @Mock
    UserPointRepository userPointRepository;

    @InjectMocks
    UserPointService userPointService;

    // test data
    UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(1L, 1L, 100);
    }

    @Test
    public void updatePoint_Spend100_Minus100Amount() {
        when(userPointRepository.addPoint(1, -100)).thenReturn(1);
        assertDoesNotThrow(() -> userPointService.changeUserPoint(1, 100, PointType.SPEND));
        verify(userPointRepository, times(1)).addPoint(1, -100);
    }

    @Test
    public void updatePoint_Charge100_Charge100Amount() {
        when(userPointRepository.addPoint(1, 100)).thenReturn(1);
        assertDoesNotThrow(() -> userPointService.changeUserPoint(1, 100, PointType.CHARGE));
        verify(userPointRepository, times(1)).addPoint(1, 100);
    }
}
