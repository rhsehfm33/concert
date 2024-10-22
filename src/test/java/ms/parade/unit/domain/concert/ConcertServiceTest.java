package ms.parade.unit.domain.concert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ms.parade.domain.concert.ConcertRepository;
import ms.parade.domain.concert.ConcertSchedule;
import ms.parade.domain.concert.ConcertService;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {
    @Mock
    ConcertRepository concertRepository;

    @InjectMocks
    ConcertService concertService;

    @Test
    public void addAvailableSeats_MinusTo0_Success() {
        ConcertSchedule concertSchedule = new ConcertSchedule(1, 1, 50, 2, LocalDate.now());
        assertDoesNotThrow(() -> concertService.addAvailableSeats(concertSchedule.id(), -2));
        verify(concertRepository).updateScheduleAvailableSeats(concertSchedule.id(), -2);
    }
}
