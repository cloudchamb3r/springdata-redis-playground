package org.cloudchamb3r.playground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

@Configuration
public class AppConfig {
    @Bean
    @Primary
    public RedisConnectionFactory lettuceStandaloneConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//                .useSsl().and() // use ssl
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build();
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(
                "localhost", 6379
        ), clientConfig);
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(
                "localhost", 6379
        ));
    }
}
