package org.opensourceway.uvp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.Table;
import org.opensourceway.uvp.enums.EventType;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "event_uk", columnList = "range_id, type, value", unique = true),
        @Index(name = "range_id_idx", columnList = "range_id")
})
public class AffectedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Convert(converter = EventType.EventTypeConverter.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private EventType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String value;

    /**
     * Range that an event belongs to.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "range_id", foreignKey = @ForeignKey(name = "range_id_fk"))
    @JsonIgnore
    private AffectedRange range;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AffectedRange getRange() {
        return range;
    }

    public void setRange(AffectedRange range) {
        this.range = range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffectedEvent that = (AffectedEvent) o;
        return type == that.type &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}