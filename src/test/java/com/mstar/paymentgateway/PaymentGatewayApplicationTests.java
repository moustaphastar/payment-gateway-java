package com.mstar.paymentgateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentGatewayApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("sample test");
        String testStringOne = "test string one";
        String testStringTwo = "test string two";
        Assertions.assertNotEquals(testStringOne, testStringTwo);
    }
}
