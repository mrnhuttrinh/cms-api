package com.ecash.cmsapi.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApi extends BaseApi {

    @GetMapping(value = "/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello");
    }
}
