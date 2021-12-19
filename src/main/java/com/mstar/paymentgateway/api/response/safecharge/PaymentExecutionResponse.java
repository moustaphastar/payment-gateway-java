package com.mstar.paymentgateway.api.response.safecharge;

import com.mstar.paymentgateway.api.response.BaseApiResponse;
import com.safecharge.response.PaymentResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentExecutionResponse extends BaseApiResponse {
    private PaymentResponse response;

    public PaymentExecutionResponse(PaymentResponse response) {
        super("SUCCESS", "0");
        this.response = response;
    }

    public PaymentExecutionResponse(String errorCode, String errorMessage) {
        super("ERROR", errorCode, errorMessage);
    }
}
