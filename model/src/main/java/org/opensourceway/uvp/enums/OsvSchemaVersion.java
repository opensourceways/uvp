package org.opensourceway.uvp.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

public enum OsvSchemaVersion {
    V1_3_0("1.3.0");

    private final String version;

    OsvSchemaVersion(String version) {
        this.version = version;
    }

    @JsonValue
    public String getVersion() {
        return version;
    }

    @Converter(autoApply = true)
    public static class OsvSchemaVersionConverter implements AttributeConverter<OsvSchemaVersion, String> {

        @Override
        public String convertToDatabaseColumn(OsvSchemaVersion osvSchemaVersion) {
            return Objects.nonNull(osvSchemaVersion) ? osvSchemaVersion.name() : null;
        }

        @Override
        public OsvSchemaVersion convertToEntityAttribute(String dbData) {
            if (Objects.isNull(dbData)) {
                return null;
            }
            return OsvSchemaVersion.valueOf(dbData);
        }
    }
}
