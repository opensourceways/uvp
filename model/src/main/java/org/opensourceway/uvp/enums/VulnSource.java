package org.opensourceway.uvp.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

public enum VulnSource {
    OSV(0),

    NVD(1),

    OSS_INDEX(2),

    VTOPIA(2),

    PRISM_7_CAI(2),
    ;

    /**
     * The priority of the vuln source. A smaller number indicates a higher priority.
     * <p>Used by aggregator.</p>
     */
    private final Integer priority;

    VulnSource(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public boolean hasHigherPriority(VulnSource other) {
        return this.priority < other.priority;
    }

    @Converter(autoApply = true)
    public static class VulnSourceConverter implements AttributeConverter<VulnSource, String> {

        @Override
        public String convertToDatabaseColumn(VulnSource eventType) {
            return Objects.nonNull(eventType) ? eventType.name() : null;
        }

        @Override
        public VulnSource convertToEntityAttribute(String dbData) {
            if (Objects.isNull(dbData)) {
                return null;
            }
            return VulnSource.valueOf(dbData);
        }
    }
}
