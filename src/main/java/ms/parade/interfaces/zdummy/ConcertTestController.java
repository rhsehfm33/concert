package ms.parade.interfaces.zdummy;

import java.time.LocalDate;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.concert.ConcertRepository;
import ms.parade.domain.seat.SeatRepository;
import ms.parade.domain.seat.SeatStatus;
import ms.parade.infrastructure.concert.ConcertScheduleParams;
import ms.parade.infrastructure.seat.SeatParams;

@Profile({"default", "dev"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
@Transactional
public class ConcertTestController {
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    private final JdbcTemplate jdbcTemplate;

    private void resetAutoIncrement(String tableName) {
        String sql = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1";
        jdbcTemplate.execute(sql);
    }

    private void deleteAllRows(String tableName) {
        String sql = "DELETE FROM " + tableName;
        jdbcTemplate.execute(sql);
    }

    private void resetTable(String tableName) {
        deleteAllRows(tableName);
        resetAutoIncrement(tableName);
    }

    private void changePoint(long userId, long amount) {
        String sql = "UPDATE user_points SET point = ? WHERE user_id <= ?";
        jdbcTemplate.update(sql, amount, userId);
    }

    private void preset(int userAmount) {
        resetTable("concert_schedules");
        resetTable("seats");
        resetTable("seat_reservations");
        resetTable("seat_payments");
        resetTable("point_history");
        changePoint(userAmount, 0);
    }

    @PutMapping("/concerts/{userAmount}")
    void setDummyData(@PathVariable int userAmount) {
        preset(userAmount);

        ConcertScheduleParams concertScheduleParams = new ConcertScheduleParams(1, 50, 50, LocalDate.now());
        concertRepository.saveSchedule(concertScheduleParams);
        for (int i = 0; i < 26; ++i) {
            for (int j = 1; j <= 40; ++j) {
                if (i * 40 + j > userAmount) {
                    return;
                }
                String name1 = Character.toString((char)('A' + i));
                String name2 = Integer.toString(j);
                String name = name1 + name2;
                SeatParams seatParams = new SeatParams(
                    1, name, 10_000, SeatStatus.EMPTY
                );
                seatRepository.save(seatParams);
            }
        }
    }
}
