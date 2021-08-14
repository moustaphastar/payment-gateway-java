package com.mstar.paymentgateway;

import com.mstar.paymentgateway.safecharge.SafeChargeGateway;
import com.safecharge.exception.SafechargeException;
import com.safecharge.response.SafechargeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;

@SpringBootApplication
public class PaymentGatewayApplication {

    @Value("#{${safeCharge}}")
    private Map<String, String> safeChargeProps;

    public static void main(String[] args) {
        SpringApplication.run(PaymentGatewayApplication.class, args);
    }

    @EventListener({ApplicationReadyEvent.class})
    public void applicationReadyEvent() {
        System.out.println("Application ready event fired!");
    }

}
