package vn.com.hugio.product.redis;

import com.fasterxml.jackson.core.type.TypeReference;

import java.time.Duration;

public interface CacheService {
    /**
     * Set value with no duration
     *
     * @param key
     * @param value
     */
    <V> void set(String key, V value);

    /**
     * Set value with duration
     *
     * @param key
     * @param value
     * @param duration
     */
    <V> void set(String key, V value, Duration duration);

    /**
     * Get key
     *
     * @param key
     * @param typeReference
     */
    <V> V get(String key, TypeReference<V> typeReference);


    /**
     * Delete key
     *
     * @param key
     */
    void delete(String key);

    /**
     * Delete all keys
     */
    void deleteAll();
}
