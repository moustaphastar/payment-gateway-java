package com.mstar.paymentgateway.api.response.safecharge;

import com.mstar.paymentgateway.api.response.BaseApiResponse;
import com.safecharge.response.InitPaymentResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InitializePaymentResponse extends BaseApiResponse {
    private InitPaymentResponse response;

    public InitializePaymentResponse(InitPaymentResponse response) {
        super("SUCCESS", "0");
        this.response = response;
    }

    public InitializePaymentResponse(String errorCode, String errorMessage) {
        super("ERROR", errorCode, errorMessage);
    }
}
