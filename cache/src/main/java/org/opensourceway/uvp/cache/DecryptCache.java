package org.opensourceway.uvp.cache;

import org.opensourceway.uvp.cache.config.CacheProperties;
import org.opensourceway.uvp.constant.CacheConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DecryptCache {
    /**
     * @return {@link CacheProperties}
     */
    @Bean
    public CacheProperties<?, ?> decryptCacheProperties() {
        return CacheProperties.builder()
                .cacheName(CacheConstant.DECRYPT_CACHE)
                .maximumCacheSize(1000L)
                // 1 hour.
                .expireAfterWrite(60 * 60L)
                .cacheNullValue(true)
                .build();
    }
}
