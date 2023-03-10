package org.opensourceway.uvp.cache;

import org.opensourceway.uvp.cache.config.CacheProperties;
import org.opensourceway.uvp.constant.CacheConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VulnSourceConfigCache {
    /**
     * @return {@link CacheProperties}
     */
    @Bean
    public CacheProperties<?, ?> vulnSourceConfigCacheProperties() {
        return CacheProperties.builder()
                .cacheName(CacheConstant.VULN_SOURCE_CONFIG_CACHE)
                .maximumCacheSize(50L)
                // 5 minutes.
                .expireAfterWrite(5 * 60L)
                .cacheNullValue(true)
                .build();
    }
}
