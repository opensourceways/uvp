package org.opensourceway.uvp.dao;

import org.opensourceway.uvp.entity.Severity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeverityRepository extends JpaRepository<Severity, UUID> {
}