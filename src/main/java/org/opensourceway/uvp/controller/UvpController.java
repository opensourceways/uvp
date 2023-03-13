package org.opensourceway.uvp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.opensourceway.uvp.api.Example;
import org.opensourceway.uvp.api.HttpStatusCode;
import org.opensourceway.uvp.constant.UvpConstant;
import org.opensourceway.uvp.enums.ErrorCode;
import org.opensourceway.uvp.exception.InvalidPurlException;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.pojo.response.Error;
import org.opensourceway.uvp.pojo.response.PackageVulns;
import org.opensourceway.uvp.service.UvpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(path = "/uvp-api")
@OpenAPIDefinition(info = @Info(title = "UVP API", version = "0.0.1",
        description = "API to query vulnerabilities via PURL"))
public class UvpController {
    private static final Logger logger = LoggerFactory.getLogger(UvpController.class);

    @Autowired
    private UvpService uvpService;

    @Operation(summary = "Query vulnerabilities affecting a given PURL")
    @Parameter(
            name = "purl",
            in = ParameterIn.QUERY,
            description = "Package URL",
            required = true,
            example = Example.PURL_EXAMPLE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = "A JSON list of OSV schema vulnerabilities",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = OsvVulnerability.class)),
                            examples = @ExampleObject(value = Example.OSV_VULN_EXAMPLE))),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = "Invalid PURL",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class),
                            examples = @ExampleObject(value = Example.ERROR_INVALID_PURL_EXAMPLE))),
            @ApiResponse(
                    responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR,
                    description = "There was an internal error while process the request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class),
                            examples = @ExampleObject(value = Example.ERROR_INTERNAL_EXAMPLE)))
    })
    @GetMapping(value = "/query", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> query(@RequestParam String purl) {
        logger.info("Query vulns by: <{}>", purl);

        List<OsvVulnerability> result;
        try {
            result = uvpService.query(purl);
        } catch (InvalidPurlException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error(ErrorCode.INVALID_PURL, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unknown error occurs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Error(ErrorCode.UNKNOWN, "Unknown error"));
        }

        logger.info("Query successfully with result size: <{}>", result.size());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Query vulnerabilities affecting a given list of PURLs")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "A JSON list of Package URLs",
            required = true,
            content = @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(value = Example.PURL_LIST_EXAMPLE)))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = HttpStatusCode.OK,
                    description = "A JSON list of Package URLs and their corresponding OSV schema vulnerabilities",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PackageVulns.class)),
                            examples = @ExampleObject(value = Example.PKG_VULNS_EXAMPLE))),
            @ApiResponse(
                    responseCode = HttpStatusCode.BAD_REQUEST,
                    description = "There are some invalid PURLs",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class),
                            examples = @ExampleObject(value = Example.ERROR_INVALID_PURL_LIST_EXAMPLE))),
            @ApiResponse(
                    responseCode = HttpStatusCode.INTERNAL_SERVER_ERROR,
                    description = "There was an internal error while process the request",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class),
                            examples = @ExampleObject(value = Example.ERROR_INTERNAL_EXAMPLE)))
    })
    @PostMapping(value = "/queryBatch", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<?> queryBatch(@RequestBody List<String> purls) {
        logger.info("Batch query vulns by: <{}>", purls);

        if (purls.size() > UvpConstant.QUERY_BATCH_LIMIT) {
            var errorMessage = "Query size <%s> exceeds the threshold <%s>"
                    .formatted(purls.size(), UvpConstant.QUERY_BATCH_LIMIT);
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error(ErrorCode.QUERY_SIZE_EXCEEDS, errorMessage));
        }

        List<PackageVulns> result;
        try {
            result = uvpService.queryBatch(purls);
        } catch (InvalidPurlException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error(ErrorCode.INVALID_PURL, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unknown error occurs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Error(ErrorCode.UNKNOWN, "Unknown error"));
        }

        logger.info("Batch query successfully, get <{}> vulns for <{}> purls.",
                result.stream().map(PackageVulns::vulns).mapToLong(List::size).sum(), purls.size());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Hidden
    @PostMapping("/importBatch")
    public @ResponseBody ResponseEntity<?> importBatch(@RequestBody List<String> purls) {
        logger.info("Batch import... size: <{}>", purls.size());

        try {
            uvpService.importBatch(purls);
        } catch (InvalidPurlException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Error(ErrorCode.INVALID_PURL, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unknown error occurs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Error(ErrorCode.UNKNOWN, "Unknown error"));
        }

        logger.info("Batch import successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Batch import successfully");
    }
}
