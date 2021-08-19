package com.mstar.paymentgateway.payoneer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PayoneerGateway {
    private static final String serviceUrl = "https://api.sandbox.oscato.com/api/lists";

    public PayoneerGateway() {
    }

    public static String getListUrl(@NonNull final Map<String, String> payoneerKeys,
                                    final String amount) throws IOException {
        Map<String, String> customer = new HashMap<>();
        customer.put("number", "42");
        customer.put("email", "john.doe@example.com");

        Map<String, String> payment = new HashMap<>();
        payment.put("amount", amount);
        payment.put("currency", "EUR");
        payment.put("reference", "Shop 101 20-03-2016");

        Map<String, String> style = new HashMap<>();
        style.put("language", "en_US");

        Map<String, String> callback = new HashMap<>();
        callback.put("appId", "dev.atharvakulkarni.e_commerce");
        callback.put("notificationUrl", "https://dev.oscato.com/shop/notify.html");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("transactionId", "tr" + System.currentTimeMillis());
        parameters.put("integration", "MOBILE_NATIVE");
        parameters.put("channel", "MOBILE_ORDER");
        parameters.put("country", "GB");
        parameters.put("customer", customer);
        parameters.put("payment", payment);
        parameters.put("style", style);
        parameters.put("callback", callback);
        String auth = payoneerKeys.get("merchantCode") + ":" + payoneerKeys.get("token");
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(serviceUrl);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        httpPost.addHeader("Content-Type", "application/vnd.optile.payment.enterprise-v1-extensible+json");
        httpPost.addHeader("Accept", "application/vnd.optile.payment.enterprise-v1-extensible+json");
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String requestJSON = gson.toJson(parameters);
        httpPost.setEntity(new StringEntity(requestJSON, StandardCharsets.UTF_8));
        HttpResponse response = httpClient.execute(httpPost);
        String responseJSON = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        String gsonString = gson.toJson(responseJSON);
        System.out.println(gsonString);
        System.out.println(responseJSON);
        return responseJSON;
    }
}
