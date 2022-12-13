package com.nd2k.authenticationapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app")
public class ApiController {

    @GetMapping("/test")
    public ResponseEntity<String> testController() {
        return new ResponseEntity<>("Restricted url", HttpStatus.OK);
    }
}
