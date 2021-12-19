package com.mstar.paymentgateway.controller;

import com.mstar.paymentgateway.api.response.safecharge.InitializePaymentResponse;
import com.mstar.paymentgateway.api.response.safecharge.PaymentApmExecutionResponse;
import com.mstar.paymentgateway.api.response.safecharge.PaymentExecutionResponse;
import com.mstar.paymentgateway.api.response.safecharge.SessionTokenResponse;
import com.mstar.paymentgateway.dto.CheckoutDetail;
import com.mstar.paymentgateway.service.safecharge.SafeChargeService;
import com.mstar.paymentgateway.utils.SafeChargeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/safe-charge")
public class SafeChargeApiController {
    private final SafeChargeService service;

    public SafeChargeApiController(SafeChargeService service) {
        this.service = service;
    }


    @PostMapping(path = "/get-session-token")
    public SessionTokenResponse getSessionToken(@RequestBody Map<String, String> params) {
        return new SessionTokenResponse(service.getSessionToken());
    }

    @PostMapping(path = "/init-payment")
    public InitializePaymentResponse initPayment(@RequestBody CheckoutDetail checkoutDetail) {
        return new InitializePaymentResponse(service.initPayment(checkoutDetail));
    }

    @PostMapping(path = "/do-payment")
    public PaymentExecutionResponse doPayment(@RequestBody CheckoutDetail checkoutDetail) {
        return new PaymentExecutionResponse(service.executePayment(checkoutDetail));
    }

    @PostMapping(path = "/do-payment-apm")
    public PaymentApmExecutionResponse doApmPayment(@RequestBody CheckoutDetail checkoutDetail) {
        return new PaymentApmExecutionResponse(service.executePaymentAPM(checkoutDetail));
    }

    @RequestMapping(path = "notification",
            consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<?> notificationGet(@RequestParam HashMap<String, Object> parameters) {
        System.out.println("[SafeCharge]-[DMN Received via HTTP GET]");
        logAndWriteApmTestDMNParams(parameters);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping(path = "notification",
            consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<?> notificationPost(@RequestParam HashMap<String, Object> parameters) {
        System.out.println("[SafeCharge]-[DMN Received via HTTP POST]");
        logAndWriteApmTestDMNParams(parameters);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private void logAndWriteApmTestDMNParams(HashMap<String, Object> parameters) {
        SafeChargeUtils.logApmTestFlowDMNParametersToConsole(parameters);
        if (!parameters.isEmpty() && parameters.get("payment_method") != null) {
            SafeChargeUtils.writeApmTestFlowObjectsToFile(parameters, parameters.get("payment_method").toString() , "dmn");
        }
    }
}
