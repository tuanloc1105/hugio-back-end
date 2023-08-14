package vn.com.hugio.product.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.log.LOG;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

@Service
@AllArgsConstructor
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> template;

    private final ObjectMapper objectMapper;

    @Override
    public <V> void set(String key, V value) {
        try {
            String stringValue = this.objectMapper.writeValueAsString(value);
            LOG.info(
                    "[REDIS SETTING VALUE] Key ({})",
                    key
            );
            this.template.opsForValue().set(key, stringValue);
        } catch (Exception e) {
            LOG.error("[REDIS SAVE FAIL] {}", e.getMessage());
        }
    }

    @Override
    public <V> void set(String key, V value, Duration duration) {
        try {
            String stringValue = this.objectMapper.writeValueAsString(value);
            LOG.info(
                    "[REDIS SETTING VALUE WITH DURATION {}] Key ({}) in ({}) second(s)",
                    duration.toString().replace("PT", Strings.EMPTY),
                    key,
                    duration.getSeconds()
            );
            this.template.opsForValue().set(key, stringValue, duration);
        } catch (Exception e) {
            LOG.error("[REDIS SAVE FAIL] {}", e.getMessage());
        }
    }

    @Override
    public <V> V get(String key, TypeReference<V> typeReference) {
        try {
            LOG.info("[REDIS GETTING VALUE] Key ({})", key);
            return this.objectMapper.readValue((String) this.template.opsForValue().get(key),
                    typeReference);
        } catch (Exception e) {
            LOG.error("[REDIS GET VALUE FAIL] {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String get(String key) {
        try {
            LOG.info("[REDIS GETTING VALUE] Key ({})", key);
            return (String) this.template.opsForValue().get(key);
        } catch (Exception e) {
            LOG.error("[REDIS GET VALUE FAIL] {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(String key) {
        Set<String> redisKeys = template.keys("*");
        redisKeys.forEach(rKey -> {
            if (rKey.contains(key)) {
                LOG.info("[REDIS DELETING] Key ({})", rKey);
                this.template.delete(key);
            }
        });
    }

    @Override
    public void deleteAll() {
        LOG.info("[REDIS DELETING ALL KEY]");
        Set<String> redisKeys = template.keys("*");
        assert redisKeys != null;
        this.template.delete(new ArrayList<>(redisKeys));
    }

}
