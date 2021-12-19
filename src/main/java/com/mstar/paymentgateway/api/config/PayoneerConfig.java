package com.mstar.paymentgateway.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayoneerConfig {
    @Value("${payoneer.merchantCode}")
    private String merchantCode;
    @Value("${payoneer.token}")
    private String token;
}
