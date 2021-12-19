package com.mstar.paymentgateway.api.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@Data
@NoArgsConstructor
public class SafeChargeConfig {
    @Value("${sfc.merchantId}")
    private String merchantId;
    @Value("${sfc.siteId}")
    private String siteId;
    @Value("${sfc.secretKey}")
    private String secretKey;
    @Value("${sfc.notificationURL}")
    private String notificationURL;
    private String clientUniqueId = UUID.randomUUID().toString();
}
