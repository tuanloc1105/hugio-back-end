package vn.com.hugio.product.redis;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import vn.com.hugio.common.log.LOG;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    @Bean
    public LettuceConnectionFactory reidsConnectionFactory() {
        int portInteger = Integer.parseInt(this.port.replace("redis", Strings.EMPTY));
        LOG.info("CONNECTING TO REDIS: %s:%s", this.host, portInteger);
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(this.host, portInteger));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }

}
