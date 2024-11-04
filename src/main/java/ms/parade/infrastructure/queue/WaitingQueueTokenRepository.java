package ms.parade.infrastructure.queue;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenRepository {
    private static final String SORTED_SET_KEY = "waiting_queue_tokens";
    private final RedisTemplate<String, Object> redisTemplate;

    public void addToSortedSet(long id) {
        redisTemplate.opsForZSet().add(SORTED_SET_KEY, id, id);
    }

    public void removeFromSortedSet(long id) {
        redisTemplate.opsForZSet().remove(SORTED_SET_KEY, id);
    }

    public int countIdsLessThan(long id) {
        Long count = redisTemplate.opsForZSet().rank(SORTED_SET_KEY, id - 1);
        return count != null ? count.intValue() : 0;
    }

    public Set<Long> getTopIds(int count) {
        Set<Object> latestIds = redisTemplate.opsForZSet().range(SORTED_SET_KEY, 0, count - 1);
        return latestIds != null ? latestIds.stream().map(id -> (Long) id).collect(Collectors.toSet()) : Set.of();
    }
}
