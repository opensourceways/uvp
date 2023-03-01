package org.opensourceway.uvp.dao;

import org.opensourceway.uvp.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}