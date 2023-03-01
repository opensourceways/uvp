package org.opensourceway.uvp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.opensourceway.uvp.enums.Ecosystem;

import java.util.UUID;

/**
 * Describes a package.
 * <p>The entity acts as a collection of packages for which related vulnerabilities are collected.</p>
 */
@Entity
@Table(indexes = {
        @Index(name = "purl_uk", columnList = "purl", unique = true),
        @Index(name = "eco_name_idx", columnList = "ecosystem, name")
})
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Identifies the ecosystem of a package.
     */
    @Convert(converter = Ecosystem.EcosystemConverter.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private Ecosystem ecosystem;

    /**
     * Name of a package.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    /**
     * Version of a package.
     */
    @Column(columnDefinition = "TEXT")
    private String version;

    /**
     * <a href="https://github.com/package-url/purl-spec">Package URL</a> that identifies a package.
     * The Package URL SHOULD contain non-empty version attribute.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String purl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Ecosystem getEcosystem() {
        return ecosystem;
    }

    public void setEcosystem(Ecosystem ecosystem) {
        this.ecosystem = ecosystem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}