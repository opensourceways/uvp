package org.opensourceway.uvp.dao;

import org.opensourceway.uvp.entity.CpePurl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CpePurlRepository extends JpaRepository<CpePurl, UUID> {
}
