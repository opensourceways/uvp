package org.opensourceway.uvp.enums;

public enum VersionType {

    /**
     * Semantic versions as defined by <a href="https://semver.org">Semantic Versioning 2.0.0</a>.
     */
    SEMVER,

    /**
     * Arbitrary, uninterpreted version strings specific to the package ecosystem.
     */
    ECOSYSTEM,

    /**
     * Full-length Git commit hashes. The relation u < v is true when commit u is a parent of commit v.
     */
    GIT
}
