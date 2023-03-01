package org.opensourceway.uvp.helper.ecosystem;

import org.opensourceway.uvp.entity.AffectedPackage;
import org.opensourceway.uvp.enums.EventType;

import java.util.List;

/**
 * A helper that handles ecosystem-related operations, such as ordering, naming.
 */
public interface EcosystemHelper {

    /**
     * Normalize purl according to the ecosystem-specific rules.
     * <p>The normalized purl MUST follow the same rules as applied to data stored in {@link AffectedPackage#getPurl()}</p>
     *
     * @param purl A standard Package URL.
     * @return Normalized Package URL.
     */
    String normalizePurl(String purl);

    /**
     * Check whether a given version is affected.
     *
     * @param version          A given version.
     * @param affectedVersions List of versions (MAY partial) that are affected.
     * @return true if version is empty or the given version is affected, otherwise false.
     */
    boolean isAffectedVersion(String version, List<String> affectedVersions);

    /**
     * Check whether a given version is affected.
     *
     * @param version          A given version.
     * @param introduced       {@link EventType#INTRODUCED}
     * @param fixed            {@link EventType#FIXED}
     * @param lastAffected     {@link EventType#LAST_AFFECTED}
     * @param limit            {@link EventType#LIMIT}
     * @return true if version is empty or the given version is affected, otherwise false.
     */
    boolean isAffectedVersion(String version, String introduced, String fixed, String lastAffected, String limit);
}
