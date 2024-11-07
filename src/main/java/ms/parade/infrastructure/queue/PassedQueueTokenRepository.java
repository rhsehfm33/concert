package ms.parade.infrastructure.queue;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PassedQueueTokenRepository {
    private static final String SORTED_SET_KEY = "passed_queue_tokens";
    private final RedisTemplate<String, Object> redisTemplate;

    public void addToSortedSet(String uuid, long timeStamp) {
        redisTemplate.opsForZSet().add(SORTED_SET_KEY, uuid, timeStamp);
    }

    public void removeFromSortedSet(String uuid) {
        redisTemplate.opsForZSet().remove(SORTED_SET_KEY, uuid);
    }

    public Set<String > getTopIds(int count) {
        Set<Object> latestIds = redisTemplate.opsForZSet().range(SORTED_SET_KEY, 0, count - 1);
        return latestIds != null ? latestIds.stream().map(id -> (String) id).collect(Collectors.toSet()) : Set.of();
    }
}
