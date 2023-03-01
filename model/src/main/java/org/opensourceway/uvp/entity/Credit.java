package org.opensourceway.uvp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "credit_uk", columnList = "vuln_id, name", unique = true),
        @Index(name = "credit_vuln_id_idx", columnList = "vuln_id")
})
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The name, label, or other identifier of the individual or entity being credited.
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    /**
     * Valid, fully qualified, plain-text URL at which the credited can be reached.
     */
    @Column(columnDefinition = "TEXT[]")
    @Type(ListArrayType.class)
    private List<String> contacts;

    /**
     * Vulnerability that a credit is given.
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
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
        Credit credit = (Credit) o;
        return Objects.equals(name, credit.name) &&
                Objects.equals(contacts, credit.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, contacts);
    }
}