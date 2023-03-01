package org.opensourceway.uvp.pojo.response;

import org.opensourceway.uvp.enums.ErrorCode;

/**
 * Error code and message for REST API.
 * @see ErrorCode
 */
public record ErrorMessage(ErrorCode code, String message) {}
