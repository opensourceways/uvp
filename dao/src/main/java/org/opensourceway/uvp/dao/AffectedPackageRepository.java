package org.opensourceway.uvp.dao;

import org.opensourceway.uvp.entity.AffectedPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AffectedPackageRepository extends JpaRepository<AffectedPackage, UUID> {
    // Filter vulnerabilities that are withdrawn.
    @Query(value = """
            SELECT ap.*
            FROM vulnerability v
                JOIN affected_package ap ON v.id = ap.vuln_id
                JOIN vuln_source_config vsc ON v.source = vsc.source
            WHERE
                v.withdrawn IS NULL
                AND vsc.query_enabled is true
                AND ap.purl = :purl
            """,
            nativeQuery = true)
    List<AffectedPackage> findByPurl(String purl);
}