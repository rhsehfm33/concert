package ms.parade.integration.interfaces.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import ms.parade.domain.point.UserPoint;
import ms.parade.domain.point.UserPointRepository;
import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenRepository;
import ms.parade.domain.queue.QueueTokenStatus;
import ms.parade.infrastructure.point.UserPointParams;
import ms.parade.infrastructure.queue.QueueTokenParams;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // test 메서드마다 db reset
public class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QueueTokenRepository queueTokenRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    @Test
    public void getUserPoint_Success() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UserPoint userPoint = userPointRepository.save(new UserPointParams(1, 0));

        QueueTokenParams queueTokenParams = new QueueTokenParams(
            userPoint.userId(), now, null, QueueTokenStatus.PASS
        );
        QueueToken queueToken = queueTokenRepository.save(queueTokenParams);

        mockMvc.perform(get("/v1/protected/users/point")
            .header("Authorization", queueToken.uuid()))
            .andExpect(status().isOk())
            .andExpect(content().string(Matchers.containsString("point")))
            .andExpect(content().string(Matchers.containsString("0")));
    }

    @Test
    public void getUserPoint_WrongUuid_Status401() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        UserPoint userPoint = userPointRepository.save(new UserPointParams(1, 0));

        QueueTokenParams queueTokenParams = new QueueTokenParams(
            userPoint.userId(), now, null, QueueTokenStatus.PASS
        );
        QueueToken queueToken = queueTokenRepository.save(queueTokenParams);

        mockMvc.perform(get("/v1/protected/users/point")
                .header("Authorization", queueToken.uuid() + "1"))
            .andExpect(status().isUnauthorized());
    }
}
