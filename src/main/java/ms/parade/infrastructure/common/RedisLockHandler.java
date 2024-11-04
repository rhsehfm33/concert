package ms.parade.infrastructure.common;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLockHandler {
    private final RedissonClient redissonClient;

    static String REDISSON_KEY_PREFIX = "RLOCK:";

    public <T> T runOnLock(String key, Long waitTime, Long leaseTime, Supplier<T> execute) {
        T result = null;

        RLock lock = redissonClient.getLock(REDISSON_KEY_PREFIX + key);
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (acquired) {
                result = execute.get();
            } else {
                throw new RuntimeException("lock key " + REDISSON_KEY_PREFIX + key + " not acquired");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return result;
    }
}
