package org.opensourceway.uvp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import org.opensourceway.uvp.enums.ReferenceType;

import java.util.Objects;
import java.util.UUID;

/**
 * Describes a vulnerability reference.
 */
@Entity
@Table(indexes = {
        @Index(name = "ref_uk", columnList = "vuln_id, type, url", unique = true),
        @Index(name = "ref_vuln_id_idx", columnList = "vuln_id")
})
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Reference type.
     */
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private ReferenceType type;

    /**
     * The URI pointing to detail of a vulnerability. This can also be used to derive the source of this information.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    /**
     * Vulnerability related to a reference.
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

    public ReferenceType getType() {
        return type;
    }

    public void setType(ReferenceType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        Reference reference = (Reference) o;
        return type == reference.type &&
                Objects.equals(url, reference.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url);
    }
}
