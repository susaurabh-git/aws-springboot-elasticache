package us.innodea.aws.springboot.elasticache.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/***
 * NOTE: This file is need only if we want to use template otherwise spring Autoconfiguration will take care of creating
 * Redis bean and its configuration, we just need to provide following properties in Application.yml file
 * spring.redis.host
 * spring.redis.port
 * spring.redis.password (Only if redis instance needs, it is not needed for AWS elasticache)
 */
@Configuration
@EnableCaching
@Slf4j
@ConfigurationProperties(prefix = "config.redis")
public class RedisConfig {

  public static final String STRING_REDIS_TEMPLATE_QUALIFIER =
      "us.innodea.aws.springboot.elasticache.config.RedisConfig#stringRedisTemplate";

  @Value("${spring.redis.host:localhost")
  private String host;

  @Value("${spring.redis.port:6379")
  private Integer port;

  @Value("${config.redis.duration:3600}")
  private Long duration;

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration =
        new RedisStandaloneConfiguration(host, port);
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean(STRING_REDIS_TEMPLATE_QUALIFIER)
  public RedisTemplate<String, Object> stringRedisTemplate(
      JedisConnectionFactory jedisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    log.info("cache duration {}", duration);
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(duration))
        .disableCachingNullValues();
  }

  @Bean
  public RedisCacheManager cacheManager(
      JedisConnectionFactory jedisConnectionFactory, RedisCacheConfiguration cacheConfiguration) {
    return RedisCacheManager.builder(jedisConnectionFactory)
        .cacheDefaults(cacheConfiguration)
        .transactionAware()
        .build();
  }
}
