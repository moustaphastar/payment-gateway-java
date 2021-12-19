package com.mstar.paymentgateway.api.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
public class PayoneerConfig {
    @Value("${payoneer.merchantCode}")
    private String merchantCode;
    @Value("${payoneer.token}")
    private String token;
    @Value("${payoneer.listURLEndpoint}")
    private String listURLEndpoint;
    @Value("${payoneer.notificationURL}")
    private String notificationURL;
}
