package com.mstar.paymentgateway.api.request.payoneer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetListURLRequest {
    private String customerNumber = "42";
    private String customerEmail = "john.doe@example.com";
    private String country = "GB";
    private String paymentAmount = "49";
    private String paymentCurrency = "EUR";
    private String paymentReference = "Shop 101 20-03-2016";
    private String styleLanguage = "en_US";
    private String callbackAppId = "dev.atharvakulkarni.e_commerce";
    private String callbackNotificationUrl;
}
