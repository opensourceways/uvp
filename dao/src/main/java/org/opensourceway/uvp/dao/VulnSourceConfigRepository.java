package org.opensourceway.uvp.dao;

import org.opensourceway.uvp.constant.CacheConstant;
import org.opensourceway.uvp.entity.VulnSourceConfig;
import org.opensourceway.uvp.enums.VulnSource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface VulnSourceConfigRepository extends JpaRepository<VulnSourceConfig, UUID> {
    @Cacheable(value = {CacheConstant.VULN_SOURCE_CONFIG_CACHE})
    @Query(value = "SELECT * FROM vuln_source_config WHERE source = :#{#source?.name()}",
            nativeQuery = true)
    Optional<VulnSourceConfig> findBySource(VulnSource source);
}
