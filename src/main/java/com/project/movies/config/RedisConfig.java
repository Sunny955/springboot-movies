package com.project.movies.config;

// @Configuration
// public class RedisConfig {

// @Bean
// public RedisConnectionFactory redisConnectionFactory() {
// return new LettuceConnectionFactory();
// }

// @Bean
// public RedisTemplate<String, Object> redisTemplate() {
// RedisTemplate<String, Object> template = new RedisTemplate<>();
// template.setConnectionFactory(redisConnectionFactory());
// template.setKeySerializer(new StringRedisSerializer());
// template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
// return template;
// }

// @Bean
// public RedisCacheManager cacheManagerRedis() {
// RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
// .entryTtl(Duration.ofMinutes(60)) // Set cache expiration time if needed
// .serializeKeysWith(SerializationPair.fromSerializer(new
// StringRedisSerializer()))
// .serializeValuesWith(SerializationPair.fromSerializer(new
// GenericJackson2JsonRedisSerializer()));
// return RedisCacheManager.builder(redisConnectionFactory())
// .cacheDefaults(config)
// .build();
// }
// }
