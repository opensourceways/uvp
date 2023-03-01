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
import org.opensourceway.uvp.enums.CvssSeverity;
import org.opensourceway.uvp.enums.ScoringSystem;

import java.util.Objects;
import java.util.UUID;

/**
 * Describes the severity of a vulnerability.
 */
@Entity
@Table(indexes = {
        @Index(name = "severity_uk", columnList = "vuln_id, scoring_system, vector", unique = true),
        @Index(name = "severity_vuln_id_idx", columnList = "vuln_id")
})
public class Severity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The name of the scoring system to express the severity of a vulnerability.
     */
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    @Column(name = "scoring_system", columnDefinition = "TEXT", nullable = false)
    private ScoringSystem scoringSystem;

    /**
     * The score of a vulnerability calculated by the scoring system.
     */
    @Column(nullable = false)
    private Double score;

    /**
     * The vector of a vulnerability calculated by the scoring system.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String vector;

    /**
     * The severity of a vulnerability calculated by the scoring system.
     */
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private CvssSeverity severity;

    /**
     * Vulnerability that severity metrics belong to.
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

    public ScoringSystem getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(ScoringSystem scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public CvssSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(CvssSeverity severity) {
        this.severity = severity;
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
        Severity severity1 = (Severity) o;
        return scoringSystem == severity1.scoringSystem &&
                Objects.equals(score, severity1.score) &&
                Objects.equals(vector, severity1.vector) &&
                severity == severity1.severity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoringSystem, score, vector, severity);
    }
}