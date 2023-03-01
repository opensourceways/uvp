package org.opensourceway.uvp.dao;

import org.opensourceway.uvp.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PackageRepository extends JpaRepository<Package, UUID> {
    @Modifying
    @Query(value = "LOCK TABLE package", nativeQuery = true)
    void lockTable();
}