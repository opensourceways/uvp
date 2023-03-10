package org.opensourceway.uvp.cache.config;


import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.opensourceway.uvp.constant.CacheConstant;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.concurrent.TimeUnit;

public class CacheProperties<K, V> {

    /**
     * The name of cache. Required.
     * <p>Cache names are defined in {@link CacheConstant}.</p>
     *
     * @See {@link CaffeineCache#CaffeineCache}
     */
    private String cacheName;

    /**
     * TimeUnit: Second
     *
     * @see Caffeine#expireAfterAccess(long, TimeUnit)
     */
    private Long expireAfterAccess;

    /**
     * TimeUnit: Second
     *
     * @see Caffeine#expireAfterWrite(long, TimeUnit)
     */
    private Long expireAfterWrite;

    /**
     * TimeUnit: Second
     *
     * @see Caffeine#refreshAfterWrite(long, TimeUnit)
     */
    private Long refreshAfterWrite;

    /**
     * @see Caffeine#initialCapacity(int)
     */
    private Integer initialCacheSize;

    /**
     * @see Caffeine#maximumSize(long)
     */
    private Long maximumCacheSize;

    /**
     * @See {@link CaffeineCache#CaffeineCache}
     */
    private Boolean cacheNullValue;

    /**
     * Cooperate with {@link #refreshAfterWrite}
     *
     * @See {@link CacheLoader}
     */
    private CacheLoader<K, V> cacheLoader;

    public CacheProperties(String cacheName) {
        this.cacheName = cacheName;
    }

    private CacheProperties(Builder<K, V> builder) {
        setCacheName(builder.cacheName);
        setExpireAfterAccess(builder.expireAfterAccess);
        setExpireAfterWrite(builder.expireAfterWrite);
        setRefreshAfterWrite(builder.refreshAfterWrite);
        setInitialCacheSize(builder.initialCacheSize);
        setMaximumCacheSize(builder.maximumCacheSize);
        setCacheNullValue(builder.cacheNullValue);
        setCacheLoader(builder.cacheLoader);
    }


    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Long getExpireAfterAccess() {
        return expireAfterAccess;
    }

    public void setExpireAfterAccess(Long expireAfterAccess) {
        this.expireAfterAccess = expireAfterAccess;
    }

    public Long getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public void setExpireAfterWrite(Long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public Long getRefreshAfterWrite() {
        return refreshAfterWrite;
    }

    public void setRefreshAfterWrite(Long refreshAfterWrite) {
        this.refreshAfterWrite = refreshAfterWrite;
    }

    public Integer getInitialCacheSize() {
        return initialCacheSize;
    }

    public void setInitialCacheSize(Integer initialCacheSize) {
        this.initialCacheSize = initialCacheSize;
    }

    public Long getMaximumCacheSize() {
        return maximumCacheSize;
    }

    public void setMaximumCacheSize(Long maximumCacheSize) {
        this.maximumCacheSize = maximumCacheSize;
    }

    public Boolean getCacheNullValue() {
        return cacheNullValue;
    }

    public void setCacheNullValue(Boolean cacheNullValue) {
        this.cacheNullValue = cacheNullValue;
    }

    public CacheLoader<K, V> getCacheLoader() {
        return cacheLoader;
    }

    public void setCacheLoader(CacheLoader<K, V> cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    public static Builder<?, ?> builder() {
        return new Builder<>();
    }

    public static final class Builder<K, V> {
        private String cacheName;
        private Long expireAfterAccess;
        private Long expireAfterWrite;
        private Long refreshAfterWrite;
        private Integer initialCacheSize;
        private Long maximumCacheSize;
        private Boolean cacheNullValue;
        private CacheLoader<K, V> cacheLoader;

        public Builder<K, V> cacheName(String val) {
            cacheName = val;
            return this;
        }

        public Builder<K, V> expireAfterAccess(Long val) {
            expireAfterAccess = val;
            return this;
        }

        public Builder<K, V> expireAfterWrite(Long val) {
            expireAfterWrite = val;
            return this;
        }

        public Builder<K, V> refreshAfterWrite(Long val) {
            refreshAfterWrite = val;
            return this;
        }

        public Builder<K, V> initialCacheSize(Integer val) {
            initialCacheSize = val;
            return this;
        }

        public Builder<K, V> maximumCacheSize(Long val) {
            maximumCacheSize = val;
            return this;
        }

        public Builder<K, V> cacheNullValue(Boolean val) {
            cacheNullValue = val;
            return this;
        }

        public Builder<K, V> cacheLoader(CacheLoader<K, V> val) {
            cacheLoader = val;
            return this;
        }

        public CacheProperties<K, V> build() {
            return new CacheProperties<>(this);
        }
    }
}
