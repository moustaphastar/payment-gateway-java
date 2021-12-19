package com.mstar.paymentgateway.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseApiResponse {
    private String status;
    private String errorCode;
    private String errorMessage;
    private long timestamp = System.currentTimeMillis();

    public BaseApiResponse(String status, String errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    public BaseApiResponse(String status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
