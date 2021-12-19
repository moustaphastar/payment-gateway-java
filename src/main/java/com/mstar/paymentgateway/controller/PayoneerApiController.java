package com.mstar.paymentgateway.controller;

import com.mstar.paymentgateway.api.request.payoneer.GetListURLRequest;
import com.mstar.paymentgateway.service.payoneer.PayoneerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payoneer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PayoneerApiController {
    private final PayoneerService service;

    public PayoneerApiController(PayoneerService service) {
        this.service = service;
    }

    @PostMapping(path = "/list-url")
    public ResponseEntity<?> listUrl(@RequestBody GetListURLRequest request) {
        String listURL = service.getListURL(request);
        return new ResponseEntity<>(listURL, HttpStatus.OK);
    }
}
