package org.opensourceway.uvp.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.hibernate.annotations.Type;
import org.opensourceway.uvp.enums.VulnSource;

import java.util.Map;

/**
 * Configurations of a vulnerability source.
 */
@Entity
public class VulnSourceConfig {
    /**
     * The source database from which a vulnerability was obtained.
     */
    @Id
    @Enumerated(EnumType.STRING)
    @Convert(converter = VulnSource.VulnSourceConverter.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private VulnSource source;

    /**
     * Whether importing vulnerabilities from the source.
     * By default, open source databases are enabled, and commercial or private databases are disabled.
     */
    private Boolean importEnabled;

    /**
     * Cron-expression of the job that imports vulnerabilities from the source.
     */
    @Column(columnDefinition = "TEXT")
    private String importCron;

    /**
     * Whether subscribing vulnerabilities from the source if it publishes vulnerabilities.
     * By default, open source databases are enabled, and commercial or private databases are disabled.
     */
    private Boolean subscribeEnabled;

    /**
     * Whether querying vulnerabilities from local database that are imported from the source.
     * By default, open source databases are enabled, and commercial or private databases are disabled.
     */
    private Boolean queryEnabled;

    /**
     * Configurations about a source, such as token, API server/endpoint.
     * <p>1. For open source databases, public information, such as API server/endpoint, is configured in
     * vuln-resources.properties, and private information, such as password and token, is stored here.</p>
     * <p>2. For commercial or private databases, all information is stored here.</p>
     *
     * @see ConfigKey
     */
    @Column(columnDefinition = "JSONB")
    @Type(value = JsonBinaryType.class)
    private Map<String, String> config;

    public VulnSource getSource() {
        return source;
    }

    public void setSource(VulnSource source) {
        this.source = source;
    }

    public Boolean getImportEnabled() {
        return importEnabled;
    }

    public void setImportEnabled(Boolean importEnabled) {
        this.importEnabled = importEnabled;
    }

    public String getImportCron() {
        return importCron;
    }

    public void setImportCron(String importCron) {
        this.importCron = importCron;
    }

    public Boolean getSubscribeEnabled() {
        return subscribeEnabled;
    }

    public void setSubscribeEnabled(Boolean subscribeEnabled) {
        this.subscribeEnabled = subscribeEnabled;
    }

    public Boolean getQueryEnabled() {
        return queryEnabled;
    }

    public void setQueryEnabled(Boolean queryEnabled) {
        this.queryEnabled = queryEnabled;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    /**
     * Keys of configurations.
     */
    public enum ConfigKey {
        TOKEN
    }
}
