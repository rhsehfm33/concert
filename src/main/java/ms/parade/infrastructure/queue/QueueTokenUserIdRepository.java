package ms.parade.infrastructure.queue;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueueTokenUserIdRepository {
    private final static String HASH_KEY = "queue_tokens_user_id";
    private final RedisTemplate<String, Object> redisTemplate;

    // <user id, uuid> 추가
    public void addId(long userId, String uuid) {
        redisTemplate.opsForHash().put(HASH_KEY, String.valueOf(userId), uuid);
    }

    // user
    public void removeId(long id) {
        redisTemplate.opsForHash().delete(HASH_KEY, String.valueOf(id));
    }

    // 특정 ID가 존재하는지 확인
    public String getIdValue(long userId) {
        return (String) redisTemplate.opsForHash().get(HASH_KEY, String.valueOf(userId));
    }
}
