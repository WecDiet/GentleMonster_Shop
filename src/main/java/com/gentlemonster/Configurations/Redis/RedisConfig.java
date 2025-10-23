package com.gentlemonster.Configurations.Redis;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@EnableCaching
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;
	@Value("${spring.data.redis.port}")
	private int redisPort;
	@Value("${spring.data.redis.password}")
	private String redisPassword;
	@Value("${spring.data.redis.timeout}")
	private long timeout;


	@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        log.info("Configuring Redis: Host={}, Port={}", redisHost, redisPort);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword != null ? RedisPassword.of(redisPassword) : null);

        GenericObjectPoolConfig genericPoolConfig = new GenericObjectPoolConfig();
        genericPoolConfig.setMaxTotal(20);  // Max connections
        genericPoolConfig.setMaxIdle(10);
        genericPoolConfig.setMinIdle(2);

        LettucePoolingClientConfiguration poolingConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeout))
                .poolConfig(genericPoolConfig)
                .build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, poolingConfig);
        factory.afterPropertiesSet();  // Init để test connect
        log.info("Connected to Redis: {}:{} successfully", redisHost, redisPort);
        return factory;
}
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serializers
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(redisObjectMapper());
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        log.info("RedisTemplate initialized successfully");
        return template;
    }



	@Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Cấu hình cho LocalDateTime
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(module);
        
        // Các cấu hình khác
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        
        return objectMapper;
    }

}

