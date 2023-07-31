package com.goodjob.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

@Configuration
public class RedisRepositoryConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // lettuce
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return redisStandaloneConfiguration;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            final RedisStandaloneConfiguration redisStandaloneConfiguration) {
        final SocketOptions socketOptions =
                SocketOptions.builder().connectTimeout(Duration.of(10, ChronoUnit.MINUTES)).build();

        final var clientOptions =
                ClientOptions.builder().socketOptions(socketOptions).autoReconnect(true).build();

        var clientConfig =
                LettuceClientConfiguration.builder()
                        .clientOptions(clientOptions)
                        .readFrom(REPLICA_PREFERRED);


        return new LettuceConnectionFactory(
                redisStandaloneConfiguration, clientConfig.build());
    }


    @Primary
    @Bean
    public RedisTemplate<String, String> redisStringTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory(redisStandaloneConfiguration()));
        redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
    @Bean
    public RedisTemplate<String, Object> redisObjectTemplate(ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory(redisStandaloneConfiguration()));
        redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }
}