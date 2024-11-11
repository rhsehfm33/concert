package ms.parade.interfaces.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ms.parade.infrastructure",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASPECTJ, pattern = "ms.parade.infrastructure.queue.*"
    )
)
public class JpaConfig {
}
