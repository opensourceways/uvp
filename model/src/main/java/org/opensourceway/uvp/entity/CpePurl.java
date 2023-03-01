package org.opensourceway.uvp.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "cpe_purl_uk", columnList = "vendor, product", unique = true)
})
public class CpePurl {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Name of a product vendor.
     */
    @Column(columnDefinition = "TEXT")
    private String vendor;

    /**
     * Name of a product.
     */
    @Column(columnDefinition = "TEXT")
    private String product;

    /**
     * CPEs of a product.
     */
    @Column(columnDefinition = "TEXT[]")
    @Type(ListArrayType.class)
    private List<String> cpes;

    /**
     * PURLs of a product.
     */
    @Column(columnDefinition = "TEXT[]")
    @Type(ListArrayType.class)
    private List<String> purls;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public List<String> getCpes() {
        return cpes;
    }

    public void setCpes(List<String> cpes) {
        this.cpes = cpes;
    }

    public List<String> getPurls() {
        return purls;
    }

    public void setPurls(List<String> purls) {
        this.purls = purls;
    }
}
