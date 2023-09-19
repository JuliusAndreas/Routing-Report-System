package app.navigational.RoutingReportSystem.Configurations;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    public static String GLOBAL_USERS_CACHE_NAME = "users-cache";
    @Value("${redis.singleServerConfig.address}")
    String address;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(address);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
