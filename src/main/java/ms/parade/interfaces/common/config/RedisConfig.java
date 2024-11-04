package ms.parade.interfaces.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "ms.parade.infrastructure.queue")
public class RedisConfig {
}
