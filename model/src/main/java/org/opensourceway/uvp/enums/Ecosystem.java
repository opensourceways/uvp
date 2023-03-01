package org.opensourceway.uvp.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.packageurl.PackageURL;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://ossf.github.io/osv-schema/#affectedpackage-field">OSV-defined ecosystems</a>
 * <p/>
 * <a href="https://osv-vulnerabilities.storage.googleapis.com/ecosystems.txt">Ecosystems with zipped vulns at
 * https://osv-vulnerabilities.storage.googleapis.com/{ECOSYSTEM}/all.zip</a>
 */
public enum Ecosystem {
    UNKNOWN("Unknown", null),

    ALPINE("Alpine", "apk"),

    ALPINE_3_2("Alpine:v3.2", "apk"),

    ALPINE_3_3("Alpine:v3.3", "apk"),

    ALPINE_3_4("Alpine:v3.4", "apk"),

    ALPINE_3_5("Alpine:v3.5", "apk"),

    ALPINE_3_6("Alpine:v3.6", "apk"),

    ALPINE_3_7("Alpine:v3.7", "apk"),

    ALPINE_3_8("Alpine:v3.8", "apk"),

    ALPINE_3_9("Alpine:v3.9", "apk"),

    ALPINE_3_10("Alpine:v3.10", "apk"),

    ALPINE_3_11("Alpine:v3.11", "apk"),

    ALPINE_3_12("Alpine:v3.12", "apk"),

    ALPINE_3_13("Alpine:v3.13", "apk"),

    ALPINE_3_14("Alpine:v3.14", "apk"),

    ALPINE_3_15("Alpine:v3.15", "apk"),

    ALPINE_3_16("Alpine:v3.16", "apk"),

    ALPINE_3_17("Alpine:v3.17", "apk"),

    CRATES_IO("crates.io", PackageURL.StandardTypes.CARGO),

    DEBIAN("Debian", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_3_0("Debian:3.0", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_3_1("Debian:3.1", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_4_0("Debian:4.0", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_5_0("Debian:5.0", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_6_0("Debian:6.0", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_7("Debian:7", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_8("Debian:8", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_9("Debian:9", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_10("Debian:10", PackageURL.StandardTypes.DEBIAN),

    DEBIAN_11("Debian:11", PackageURL.StandardTypes.DEBIAN),

    GO("Go", PackageURL.StandardTypes.GOLANG),

    HEX("Hex", PackageURL.StandardTypes.HEX),

    MAVEN("Maven", PackageURL.StandardTypes.MAVEN),

    NPM("npm", PackageURL.StandardTypes.NPM),

    NUGET("NuGet", PackageURL.StandardTypes.NUGET),

    OSS_FUZZ("OSS-Fuzz", PackageURL.StandardTypes.GENERIC),

    PACKAGIST("Packagist", PackageURL.StandardTypes.COMPOSER),

    PUB("Pub", "pub"),

    PYPI("PyPI", PackageURL.StandardTypes.PYPI),

    RUBYGEMS("RubyGems", PackageURL.StandardTypes.GEM),

    LINUX("Linux", null),

    ANDROID("Android", null),

    GITHUB_ACTIONS("GitHub Actions", null),

    GSD("GSD", null),

    UVI("UVI", null),
    ;

    private final String ecosystem;

    private final String purlType;

    private static final Map<String, Ecosystem> ecosystemToEnum = Arrays.stream(Ecosystem.values())
            .collect(Collectors.toMap(Ecosystem::getEcosystem, Function.identity()));

    private static final Map<String, Ecosystem> purlTypeToEnum = Arrays.stream(Ecosystem.values())
            .filter(it -> Objects.nonNull(it.purlType)
                    && !StringUtils.equals(it.purlType, PackageURL.StandardTypes.GENERIC))
            // apk -> ALPINE, deb -> DEBIAN
            .collect(Collectors.toMap(Ecosystem::getPurlType, Function.identity(), (oldValue, value) -> oldValue));

    Ecosystem(String ecosystem, String purlType) {
        this.ecosystem = ecosystem;
        this.purlType = purlType;
    }

    @JsonValue
    public String getEcosystem() {
        return ecosystem;
    }

    public String getPurlType() {
        return purlType;
    }

    public static Ecosystem findByEcosystem(String ecosystem) {
        return ecosystemToEnum.get(ecosystem);
    }

    public static Ecosystem findByPurlType(String purlType) {
        return purlTypeToEnum.getOrDefault(purlType, UNKNOWN);
    }

    @Converter(autoApply = true)
    public static class EcosystemConverter implements AttributeConverter<Ecosystem, String> {

        @Override
        public String convertToDatabaseColumn(Ecosystem ecosystem) {
            return Objects.nonNull(ecosystem) ? ecosystem.ecosystem : null;
        }

        @Override
        public Ecosystem convertToEntityAttribute(String dbData) {
            if (Objects.isNull(dbData)) {
                return null;
            }
            return Optional.ofNullable(ecosystemToEnum.get(dbData))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ecosystem: %s".formatted(dbData)));
        }
    }
}
