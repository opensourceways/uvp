package org.opensourceway.uvp.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.opensourceway.uvp.utility.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Used to encrypt given text with the same encryptor of the service.
 */
@Hidden
@Controller
@RequestMapping(path = "/encrypt-api")
public class EncryptController {
    @Autowired
    private EncryptUtil encryptUtil;

    @PostMapping("/encrypt")
    public @ResponseBody ResponseEntity<String> encrypt(@RequestParam String text) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(encryptUtil.encrypt(text));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to encrypt");
        }
    }
}
