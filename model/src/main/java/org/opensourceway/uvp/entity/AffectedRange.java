package org.opensourceway.uvp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import org.opensourceway.uvp.enums.VersionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Describes the affected ranges of versions.
 */
@Entity
@Table(indexes = {
        @Index(name = "affected_pkg_id_idx", columnList = "affected_pkg_id")
})
public class AffectedRange {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The type of version range being recorded.
     */
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private VersionType type;

    /**
     * The URL of a package's source code repository.
     * <p>The field is required if {@link #type} is {@link VersionType#GIT}</p>
     */
    @Column(columnDefinition = "TEXT")
    private String repo;

    /**
     * JSON object that holds additional information useful in converting from the osv-schema
     * back into the original representation.
     */
    @Column(columnDefinition = "JSONB")
    @Type(value = JsonBinaryType.class)
    private Map<Object, Object> databaseSpecific;

    /**
     * The events of a range.
     */
    @OneToMany(mappedBy = "range", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AffectedEvent> events = new ArrayList<>();

    /**
     * Affected package that a range belongs to.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "affected_pkg_id", foreignKey = @ForeignKey(name = "affected_pkg_id_fk"))
    @JsonIgnore
    private AffectedPackage affectedPackage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public VersionType getType() {
        return type;
    }

    public void setType(VersionType type) {
        this.type = type;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public Map<Object, Object> getDatabaseSpecific() {
        return databaseSpecific;
    }

    public void setDatabaseSpecific(Map<Object, Object> databaseSpecific) {
        this.databaseSpecific = databaseSpecific;
    }

    public List<AffectedEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AffectedEvent> events) {
        if (Objects.isNull(this.events)) {
            this.events = events;
        } else {
            this.events.clear();
            this.events.addAll(events);
        }
    }

    public AffectedPackage getAffectedPackage() {
        return affectedPackage;
    }

    public void setAffectedPackage(AffectedPackage affectedPackage) {
        this.affectedPackage = affectedPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffectedRange that = (AffectedRange) o;
        return type == that.type &&
                Objects.equals(repo, that.repo) &&
                Objects.equals(databaseSpecific, that.databaseSpecific) &&
                Objects.deepEquals(events.toArray(), that.events.toArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, repo, databaseSpecific, events);
    }
}