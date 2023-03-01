package org.opensourceway.uvp.cache.config;


import com.github.benmanes.caffeine.cache.CacheLoader;

public class CacheProperties<K, V> {

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存在访问后失效时间，单位：秒
     */
    private Long expireAfterAccess;

    /**
     * 缓存在写入后失效时长，单位：秒
     */
    private Long expireAfterWrite;

    /**
     * 缓存写入后自动刷新时长，单位：秒；需要cacheLoader属性配套使用
     * <p>
     * cache的key需要是单值，且不能拼接前缀和后缀
     */
    private Long refreshAfterWrite;

    /**
     * 缓存最初大小
     */
    private Integer initialCacheSize;

    /**
     * 缓存最大对象个数
     * <p>
     * 超过此数量后，之前放入的对象将会失效
     */
    private Long maximumCacheSize;

    /**
     * 是否存储控制，默认：false，防止缓存穿透
     */
    private Boolean cacheNullValue;

    /**
     * 与缓存refreshAfterWrite配套使用
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
