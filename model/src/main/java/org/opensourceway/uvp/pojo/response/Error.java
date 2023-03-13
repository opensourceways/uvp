package org.opensourceway.uvp.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.opensourceway.uvp.enums.ErrorCode;

/**
 * Error code and message for REST API.
 * @see ErrorCode
 */
@Schema(description = "Error response")
public record Error(ErrorCode code, String message) {}
