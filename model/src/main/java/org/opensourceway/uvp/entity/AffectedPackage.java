package org.opensourceway.uvp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import org.opensourceway.uvp.enums.Ecosystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Describes a package that is affected by a vulnerability.
 * <p>The difference between this entity and {@link Package} is that this entity describes the affected packages of a
 * vulnerability while {@link Package} is independently used for searching vulnerability in vulnerability databases.</p>
 */
@Entity
@Table(indexes = {
        @Index(name = "vuln_eco_name_idx", columnList = "vuln_id, ecosystem, name"),
        @Index(name = "vuln_purl_idx", columnList = "vuln_id, purl"),
        @Index(name = "affected_pkg_vuln_id_idx", columnList = "vuln_id")
})
public class AffectedPackage {
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
     * <a href="https://github.com/package-url/purl-spec">Package URL</a> that identifies a package.
     * The Package URL SHOULD NOT contain version attribute.
     */
    @Column(columnDefinition = "TEXT")
    private String purl;

    /**
     * Affected package versions.
     */
    @Column(columnDefinition = "TEXT[]")
    @Type(ListArrayType.class)
    private List<String> versions;

    /**
     * JSON object that holds additional information about a vulnerability as defined by the ecosystem for which the
     * entry applies.
     * <p>Especially for information extracted from the canonical database about an affected package itself .</p>
     */
    @Column(columnDefinition = "JSONB")
    @Type(value = JsonBinaryType.class)
    private Map<Object, Object> ecosystemSpecific;

    /**
     * JSON object that holds additional information about a vulnerability as defined by the database from which the
     * entry was obtained.
     * <p>Especially for information about an affected package itself.</p>
     */
    @Column(columnDefinition = "JSONB")
    @Type(value = JsonBinaryType.class)
    private Map<Object, Object> databaseSpecific;

    /**
     * The ranges of an affected package.
     */
    @OneToMany(mappedBy = "affectedPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AffectedRange> ranges = new ArrayList<>();

    /**
     * Vulnerability that affects a package.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "vuln_id", foreignKey = @ForeignKey(name = "vuln_id_fk"))
    @JsonIgnore
    private Vulnerability vulnerability;

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

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public Map<Object, Object> getEcosystemSpecific() {
        return ecosystemSpecific;
    }

    public void setEcosystemSpecific(Map<Object, Object> ecosystemSpecific) {
        this.ecosystemSpecific = ecosystemSpecific;
    }

    public Map<Object, Object> getDatabaseSpecific() {
        return databaseSpecific;
    }

    public void setDatabaseSpecific(Map<Object, Object> databaseSpecific) {
        this.databaseSpecific = databaseSpecific;
    }

    public List<AffectedRange> getRanges() {
        return ranges;
    }

    public void setRanges(List<AffectedRange> ranges) {
        if (Objects.isNull(this.ranges)) {
            this.ranges = ranges;
        } else {
            this.ranges.clear();
            this.ranges.addAll(ranges);
        }
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffectedPackage that = (AffectedPackage) o;
        return ecosystem == that.ecosystem &&
                Objects.equals(name, that.name) &&
                Objects.equals(purl, that.purl) &&
                Objects.equals(versions, that.versions) &&
                Objects.equals(ecosystemSpecific, that.ecosystemSpecific) &&
                Objects.equals(databaseSpecific, that.databaseSpecific) &&
                Objects.deepEquals(ranges.toArray(), that.ranges.toArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ecosystem, name, purl, versions, ecosystemSpecific, databaseSpecific, ranges);
    }
}