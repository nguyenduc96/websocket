package com.chainprotocol.gateway.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import com.chainprotocol.gateway.constant.RedisConstant;
import com.chainprotocol.gateway.core.FastJsonRedisSerializer;

import lombok.Getter;

@Getter
@Configuration
public class CacheConfig {

    // Use the configuration from gw-data
    @Value("${cp.data.gw.redis.host}")
    private String host;

    @Value("${cp.data.gw.redis.port}")
    private Integer port;

    @Value("${cp.data.gw.redis.password}")
    private String password;

    // REGISTER
    @Bean(RedisConstant.DB.REGISTER.CONNECTION_FACTORY)
    RedisConnectionFactory registerConnectionFactory() {
        return connectionFactory(RedisConstant.DB.REGISTER.INDEX);
    }

    @Bean(RedisConstant.DB.REGISTER.REDIS_TEMPLATE)
    RedisTemplate registerRedisTemplate(@Qualifier(RedisConstant.DB.REGISTER.CONNECTION_FACTORY) RedisConnectionFactory registerConnectionFactory) {
        return redisTemplate(registerConnectionFactory);
    }

    // CACHE
    @Primary
    @Bean(RedisConstant.DB.CACHE.CONNECTION_FACTORY)
    RedisConnectionFactory cacheConnectionFactory() {
        return connectionFactory(RedisConstant.DB.CACHE.INDEX);
    }

    @Bean(RedisConstant.DB.CACHE.REDIS_TEMPLATE)
    RedisTemplate cacheRedisTemplate(@Qualifier(RedisConstant.DB.CACHE.CONNECTION_FACTORY) RedisConnectionFactory cacheConnectionFactory) {
        return redisTemplate(cacheConnectionFactory);
    }

    // MAPPING
    @Bean(RedisConstant.DB.GW_MAPPING.CONNECTION_FACTORY)
    RedisConnectionFactory mappingConnectionFactory() {
        return connectionFactory(RedisConstant.DB.GW_MAPPING.INDEX);
    }

    @Bean(RedisConstant.DB.GW_MAPPING.REDIS_TEMPLATE)
    RedisTemplate<?, ?> mappingRedisTemplate(@Qualifier(RedisConstant.DB.GW_MAPPING.CONNECTION_FACTORY) RedisConnectionFactory connectionFactory) {
        return redisTemplate(connectionFactory);
    }

    protected RedisConnectionFactory connectionFactory(int database) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(getHost());
        configuration.setPort(getPort());
        if (!StringUtils.isEmpty(getPassword())) {
            configuration.setPassword(RedisPassword.of(getPassword()));
        }
        configuration.setDatabase(database);
        return new LettuceConnectionFactory(configuration);
    }

    @SuppressWarnings("unchecked")
    protected RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer();
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        return redisTemplate;
    }
}
