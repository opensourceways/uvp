package org.opensourceway.uvp.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.opensourceway.uvp.pojo.response.ErrorMessage;

/**
 * Error code of REST API error message.
 * @see ErrorMessage
 */
public enum ErrorCode {

    UNKNOWN(1),

    INVALID_PURL(2),

    QUERY_SIZE_EXCEEDS(3),
    ;

    private final Integer code;

    ErrorCode(Integer code) {
        this.code = code;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }
}
