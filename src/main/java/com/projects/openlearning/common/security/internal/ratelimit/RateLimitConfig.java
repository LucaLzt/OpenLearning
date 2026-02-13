package com.projects.openlearning.common.security.internal.ratelimit;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.Objects;

@Configuration
public class RateLimitConfig {

    /**
     * Creates a StatefulRedisConnection for Bucket4j to use with Lettuce.
     */
    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, byte[]> bucket4jConnection(RedisConnectionFactory redisConnectionFactory) {
        // 1. Ensure that the provided RedisConnectionFactory is a LettuceConnectionFactory
        LettuceConnectionFactory lettuceFactory = (LettuceConnectionFactory) redisConnectionFactory;

        // 2. Get the native RedisClient from the LettuceConnectionFactory
        RedisClient redisClient = (RedisClient) Objects.requireNonNull(
                lettuceFactory.getNativeClient(),
                "Expected LettuceConnectionFactory to provide a RedisClient"
        );

        // 3. Create and return a StatefulRedisConnection with the appropriate codecs for Bucket4j
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    /**
     * Creates a ProxyManager for Bucket4j using the provided Redis connection.
     */
    @Bean
    public LettuceBasedProxyManager<String> proxyManager(StatefulRedisConnection<String, byte[]> connection) {
        return Bucket4jLettuce.casBasedBuilder(connection)
                .expirationAfterWrite(ExpirationAfterWriteStrategy
                        .basedOnTimeForRefillingBucketUpToMax(Duration.ofHours(1))
                )
                .build();
    }
}
