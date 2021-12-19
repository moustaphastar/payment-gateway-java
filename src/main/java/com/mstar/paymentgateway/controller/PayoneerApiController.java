package com.mstar.paymentgateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payoneer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PayoneerApiController {

    @PostMapping(path = "/list-url")
    public ResponseEntity<?> listUrl() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
