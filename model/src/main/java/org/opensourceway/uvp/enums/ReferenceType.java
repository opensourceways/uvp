package org.opensourceway.uvp.enums;

public enum ReferenceType {
    /**
     * A published security advisory for the vulnerability.
     */
    ADVISORY,

    /**
     * An article or blog post describing the vulnerability.
     */
    ARTICLE,

    /**
     * A report, typically on a bug or issue tracker, of the vulnerability.
     */
    REPORT,

    /**
     * A source code browser link to the fix (e.g., a GitHub commit) Note that the fix type is meant for
     * viewing by people using web browsers.
     */
    FIX,

    /**
     * A home web page for the package.
     */
    PACKAGE,

    /**
     * A demonstration of the validity of a vulnerability claim, e.g. app.any.run replaying the exploitation of the vulnerability.
     */
    EVIDENCE,

    /**
     * A web page of some unspecified kind.
     */
    WEB
}
