package org.opensourceway.uvp.controller;

import org.opensourceway.uvp.constant.UvpConstant;
import org.opensourceway.uvp.enums.ErrorCode;
import org.opensourceway.uvp.exception.InvalidPurlException;
import org.opensourceway.uvp.pojo.osv.OsvVulnerability;
import org.opensourceway.uvp.pojo.response.ErrorMessage;
import org.opensourceway.uvp.pojo.response.PackageVulns;
import org.opensourceway.uvp.service.UvpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/uvp-api")
public class UvpController {
    private static final Logger logger = LoggerFactory.getLogger(UvpController.class);

    @Autowired
    private UvpService uvpService;


    @GetMapping("/query/{*purl}")
    public @ResponseBody ResponseEntity<?> query(@PathVariable String purl) {
        purl = purl.substring(1);
        logger.info("Query vulns by: <{}>", purl);

        List<OsvVulnerability> result;
        try {
            result = uvpService.query(purl);
        } catch (InvalidPurlException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(ErrorCode.INVALID_PURL, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unknown error occurs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(ErrorCode.UNKNOWN, "Unknown error"));
        }

        logger.info("Query successfully with result size: <{}>", result.size());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/queryBatch")
    public @ResponseBody ResponseEntity<?> queryBatch(@RequestBody List<String> purls) {
        logger.info("Batch query vulns by: <{}>", purls);

        if (purls.size() > UvpConstant.QUERY_BATCH_LIMIT) {
            var errorMessage = "Query size <%s> exceeds the threshold <%s>"
                    .formatted(purls.size(), UvpConstant.QUERY_BATCH_LIMIT);
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(ErrorCode.QUERY_SIZE_EXCEEDS, errorMessage));
        }

        List<PackageVulns> result;
        try {
            result = uvpService.queryBatch(purls);
        } catch (InvalidPurlException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(ErrorCode.INVALID_PURL, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unknown error occurs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(ErrorCode.UNKNOWN, "Unknown error"));
        }

        logger.info("Batch query successfully, get <{}> vulns for <{}> purls.",
                result.stream().map(PackageVulns::vulns).mapToLong(List::size).sum(), purls.size());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/importBatch")
    public @ResponseBody ResponseEntity<?> importBatch(@RequestBody List<String> purls) {
        logger.info("Batch import... size: <{}>", purls.size());

        try {
            uvpService.importBatch(purls);
        } catch (InvalidPurlException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(ErrorCode.INVALID_PURL, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unknown error occurs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(ErrorCode.UNKNOWN, "Unknown error"));
        }

        logger.info("Batch import successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Batch import successfully");
    }
}
