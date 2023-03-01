package org.opensourceway.uvp.pojo.nvd;

import com.fasterxml.jackson.annotation.JsonValue;
import org.opensourceway.uvp.enums.ReferenceType;

public enum NvdReferenceTag {

    BROKEN_LINK("Broken Link", ReferenceType.WEB),
    RELEASE_NOTES("Release Notes", ReferenceType.FIX),
    VDB_ENTRY("VDB Entry", ReferenceType.WEB),
    TOOL_SIGNATURE("Tool Signature", ReferenceType.WEB),
    PERMISSIONS_REQUIRED("Permissions Required", ReferenceType.WEB),
    PRODUCT("Product", ReferenceType.PACKAGE),
    NOT_APPLICABLE("Not Applicable", ReferenceType.WEB),
    PRESS_MEDIA_COVERAGE("Press/Media Coverage", ReferenceType.WEB),
    US_GOVERNMENT_RESOURCE("US Government Resource", ReferenceType.WEB),
    MITIGATION("Mitigation", ReferenceType.FIX),
    THIRD_PARTY_ADVISORY("Third Party Advisory", ReferenceType.ADVISORY),
    URL_REPURPOSED("URL Repurposed", ReferenceType.WEB),
    MAILING_LIST("Mailing List", ReferenceType.REPORT),
    ISSUE_TRACKING("Issue Tracking", ReferenceType.REPORT),
    TECHNICAL_DESCRIPTION("Technical Description", ReferenceType.ARTICLE),
    VENDOR_ADVISORY("Vendor Advisory", ReferenceType.ADVISORY),
    EXPLOIT("Exploit", ReferenceType.EVIDENCE),
    PATCH("Patch", ReferenceType.FIX),
    ;

    private final String tag;

    private final ReferenceType osvReferenceType;

    NvdReferenceTag(String tag, ReferenceType osvReferenceType) {
        this.tag = tag;
        this.osvReferenceType = osvReferenceType;
    }

    @JsonValue
    public String getTag() {
        return tag;
    }

    public ReferenceType getOsvReferenceType() {
        return osvReferenceType;
    }
}
