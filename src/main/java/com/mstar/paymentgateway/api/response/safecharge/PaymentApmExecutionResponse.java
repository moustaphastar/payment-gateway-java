package com.mstar.paymentgateway.api.response.safecharge;

import com.mstar.paymentgateway.api.response.BaseApiResponse;
import com.safecharge.response.PaymentResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentApmExecutionResponse extends BaseApiResponse {
    private PaymentResponse response;

    public PaymentApmExecutionResponse(PaymentResponse response) {
        super("SUCCESS", "0");
        this.response = response;
    }

    public PaymentApmExecutionResponse(String errorCode, String errorMessage) {
        super("ERROR", errorCode, errorMessage);
    }
}
