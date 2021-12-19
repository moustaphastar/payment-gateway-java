package com.mstar.paymentgateway.service.payoneer.Impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mstar.paymentgateway.api.config.PayoneerConfig;
import com.mstar.paymentgateway.api.request.payoneer.GetListURLRequest;
import com.mstar.paymentgateway.service.payoneer.PayoneerService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayoneerServiceImpl implements PayoneerService {
    private final PayoneerConfig config;

    public PayoneerServiceImpl(PayoneerConfig config) {
        this.config = config;
    }

    @Override
    public String getListURL(final GetListURLRequest request) {
        String requestBodyJsonPayload = buildRequestBody(request);
        if (requestBodyJsonPayload != null) {
            HttpRequest httpRequest = buildHttpRequest(requestBodyJsonPayload);
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
                return response.body();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private HttpRequest buildHttpRequest(String requestBody) {
        String auth = config.getMerchantCode() + ":" + config.getToken();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
        return HttpRequest
                .newBuilder()
                .uri(URI.create(config.getListURLEndpoint()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/vnd.optile.payment.enterprise-v1-extensible+json")
                .header("Accept", "application/vnd.optile.payment.enterprise-v1-extensible+json")
                .header("Authorization", encodedAuth)
                .build();
    }

    private String buildRequestBody(GetListURLRequest request) {
        Map<String, String> customer = new HashMap<>();
        customer.put("number", request.getCustomerNumber());
        customer.put("email", request.getCustomerEmail());

        Map<String, String> payment = new HashMap<>();
        payment.put("amount", request.getPaymentAmount());
        payment.put("currency", request.getPaymentCurrency());
        payment.put("reference", request.getPaymentReference());

        Map<String, String> style = new HashMap<>();
        style.put("language", request.getStyleLanguage());

        Map<String, String> callback = new HashMap<>();
        callback.put("appId", request.getCallbackAppId());
        callback.put("notificationUrl", config.getNotificationURL());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("transactionId", "tr" + System.currentTimeMillis());
        parameters.put("integration", "MOBILE_NATIVE");
        parameters.put("channel", "MOBILE_ORDER");
        parameters.put("country", request.getCountry());
        parameters.put("customer", customer);
        parameters.put("payment", payment);
        parameters.put("style", style);
        parameters.put("callback", callback);

        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
