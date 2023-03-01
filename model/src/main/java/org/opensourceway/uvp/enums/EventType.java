package org.opensourceway.uvp.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EventType {

    /**
     * The version when a vulnerability is introduced.
     */
    INTRODUCED("introduced"),

    /**
     * The version when a vulnerability is fixed.
     */
    FIXED("fixed"),

    /**
     * The last known affected version.
     */
    LAST_AFFECTED("last_affected"),

    /**
     * An upper limit on the range being described.
     * <p>The field SHOULD NOT be used for numbered version ranges.</p>
     * <p>The field SHOULD be used for {@link VersionType#GIT} type version ranges.</p>
     */
    LIMIT("limit");

    private final String value;

    private static final Map<String, EventType> valueToEnum = Arrays.stream(EventType.values())
            .collect(Collectors.toMap(EventType::getValue, Function.identity()));

    EventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Converter(autoApply = true)
    public static class EventTypeConverter implements AttributeConverter<EventType, String> {

        @Override
        public String convertToDatabaseColumn(EventType eventType) {
            return Objects.nonNull(eventType) ? eventType.value : null;
        }

        @Override
        public EventType convertToEntityAttribute(String dbData) {
            if (Objects.isNull(dbData)) {
                return null;
            }
            return Optional.ofNullable(valueToEnum.get(dbData))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid eventType: %s".formatted(dbData)));
        }
    }
}
