package com.mstar.paymentgateway.api.response.safecharge;

import com.mstar.paymentgateway.api.response.BaseApiResponse;
import com.safecharge.response.GetSessionTokenResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SessionTokenResponse extends BaseApiResponse {
    private GetSessionTokenResponse response;

    public SessionTokenResponse(GetSessionTokenResponse response) {
        super("SUCCESS", "0");
        this.response = response;
    }

    public SessionTokenResponse(String errorCode, String errorMessage) {
        super("ERROR", errorCode, errorMessage);
    }
}
