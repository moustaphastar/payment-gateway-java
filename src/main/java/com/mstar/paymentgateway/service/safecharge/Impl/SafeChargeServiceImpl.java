package com.mstar.paymentgateway.service.safecharge.Impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mstar.paymentgateway.api.config.SafeChargeConfig;
import com.mstar.paymentgateway.dto.CheckoutDetail;
import com.mstar.paymentgateway.service.safecharge.SafeChargeService;
import com.mstar.paymentgateway.utils.SafeChargeUtils;
import com.safecharge.biz.SafechargeRequestExecutor;
import com.safecharge.model.*;
import com.safecharge.request.*;
import com.safecharge.response.GetSessionTokenResponse;
import com.safecharge.response.InitPaymentResponse;
import com.safecharge.response.PaymentResponse;
import com.safecharge.response.SafechargeResponse;
import com.safecharge.util.APIConstants;
import com.safecharge.util.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class SafeChargeServiceImpl implements SafeChargeService {
    private final SafeChargeConfig config;
    private final MerchantInfo MERCHANT_INFO;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SafeChargeServiceImpl(SafeChargeConfig config) {
        this.config = config;
        this.MERCHANT_INFO = initMerchantInfo();
    }

    private MerchantInfo initMerchantInfo() {
        return new MerchantInfo(config.getSecretKey(),
                config.getMerchantId(),
                config.getSiteId(),
                APIConstants.INTEGRATION_HOST,
                Constants.HashAlgorithm.SHA256);
    }

    @Override
    public GetSessionTokenResponse getSessionToken() {
        SafechargeBaseRequest request = new GetSessionTokenRequest.Builder().addMerchantInfo(MERCHANT_INFO).build();
        log.info("[SafeCharge]-[GetSessionToken] - [Request] {}", request);
        System.out.println(gson.toJson(request));
        SafechargeRequestExecutor executor = SafechargeRequestExecutor.getInstance();
        GetSessionTokenResponse response = (GetSessionTokenResponse) executor.executeRequest(request);
        log.info("[SafeCharge] [GetSessionToken] - [Response] {}", response);
        System.out.println(gson.toJson(response));
        return response;
    }

    @Override
    public InitPaymentResponse initPayment(CheckoutDetail checkoutDetail) {
        InitPaymentRequest request = (InitPaymentRequest) prepareRequest(InitPaymentRequest.class, checkoutDetail);
        SafechargeRequestExecutor executor = SafechargeRequestExecutor.getInstance();
        InitPaymentResponse response = (InitPaymentResponse) executor.executeRequest(request);
        logResponseToConsole(response);
        return response;
    }

    @Override
    public PaymentResponse executePayment(CheckoutDetail checkoutDetail) {
        PaymentRequest request = (PaymentRequest) prepareRequest(PaymentRequest.class, checkoutDetail);
        SafechargeRequestExecutor executor = SafechargeRequestExecutor.getInstance();
        PaymentResponse response = (PaymentResponse) executor.executeRequest(request);
        logResponseToConsole(response);
        return response;
    }

    @Override
    public PaymentResponse executePaymentAPM(CheckoutDetail checkoutDetail) {
        PaymentRequest request = (PaymentRequest) prepareRequest(PaymentAPMRequest.class, checkoutDetail);
        SafeChargeUtils.writeApmTestFlowObjectsToFile(request, checkoutDetail.getApmPaymentMethod(), "request");
        SafechargeRequestExecutor executor = SafechargeRequestExecutor.getInstance();
        PaymentResponse response = (PaymentResponse) executor.executeRequest(request);
        logResponseToConsole(response);
        SafeChargeUtils.writeApmTestFlowObjectsToFile(response, checkoutDetail.getApmPaymentMethod(), "response");
        return response;
    }

    private RestApiUserDetails prepareUserDetails(CheckoutDetail checkoutDetail) {
        //UserDetails userDetails = new UserDetails();
        RestApiUserDetails userDetails = new RestApiUserDetails();
        userDetails.setFirstName("Mack");
        userDetails.setLastName("Starton");
        userDetails.setEmail("mackstarton@gmail.com");
        userDetails.setCountry(checkoutDetail.getCountry());
        userDetails.setCity("New York");
        userDetails.setAddress("Abraham Lincoln Street 234");
        userDetails.setZip("2341234");
        userDetails.setPhone("+13372334554");
        return userDetails;
    }

    private DeviceDetails prepareDeviceDetails() {
        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setIpAddress("127.0.0.1");
        return deviceDetails;
    }

    private BrowserDetails prepareBrowserDetails() {
        BrowserDetails browserDetails = new BrowserDetails();
        browserDetails.setIp("127.0.0.1");
        browserDetails.setJavaScriptEnabled("true");
        return browserDetails;
    }

    private V2AdditionalParams prepareV2AdditionalParams() {
        V2AdditionalParams params = new V2AdditionalParams();
        params.setChallengeWindowSize("5");
        return params;
    }

    private ThreeD prepareThreeD(String methodURL) {
        ThreeD threeD = new ThreeD();
        threeD.setBrowserDetails(prepareBrowserDetails());
        threeD.setV2AdditionalParams(prepareV2AdditionalParams());
        threeD.setVersion("2");
        threeD.setNotificationURL(config.getNotificationURL());
        threeD.setMerchantURL(methodURL);
        threeD.setPlatformType("02");
        return threeD;
    }

    private InitPaymentThreeD prepareInitPaymentThreeD() {
        InitPaymentThreeD initPaymentThreeD = new InitPaymentThreeD();
        initPaymentThreeD.setMethodNotificationUrl(config.getNotificationURL());
        return initPaymentThreeD;
    }

    private Card prepareCard(CheckoutDetail checkoutDetail, ThreeD threeD) {
        Card card = new Card();
        card.setCardNumber(checkoutDetail.getCardNumber());
        card.setCardHolderName(checkoutDetail.getCardholder());
        card.setCVV(checkoutDetail.getCvv());
        card.setExpirationMonth(checkoutDetail.getExpiryMonth());
        card.setExpirationYear(checkoutDetail.getExpiryYear());
        card.setThreeD(threeD);
        return card;
    }

    private InitPaymentCard prepareInitPaymentCard(CheckoutDetail checkoutDetail, InitPaymentThreeD initPaymentThreeD) {
        InitPaymentCard initPaymentCard = new InitPaymentCard();
        initPaymentCard.setCardNumber(checkoutDetail.getCardNumber());
        initPaymentCard.setCardHolderName(checkoutDetail.getCardholder());
        initPaymentCard.setCVV(checkoutDetail.getCvv());
        initPaymentCard.setExpirationMonth(checkoutDetail.getExpiryMonth());
        initPaymentCard.setExpirationYear(checkoutDetail.getExpiryYear());
        initPaymentCard.setThreeD(initPaymentThreeD);
        return initPaymentCard;
    }

    private InitPaymentPaymentOption prepareInitPaymentOption(InitPaymentCard initPaymentCard) {
        InitPaymentPaymentOption initPaymentPaymentOption = new InitPaymentPaymentOption();
        initPaymentPaymentOption.setCard(initPaymentCard);
        return initPaymentPaymentOption;
    }

    private PaymentOption preparePaymentOption(CheckoutDetail checkoutDetail) {
        PaymentOption paymentOption = new PaymentOption();
        paymentOption.setCard(prepareCard(checkoutDetail, prepareThreeD("methodURL")));
        return paymentOption;
    }

    private PaymentOption preparePaymentAPMOption(CheckoutDetail checkoutDetail) {
        PaymentOption paymentOption = new PaymentOption();
        Map<String, String> apmOptions = checkoutDetail.getApmOptions();
        if (apmOptions == null) {
            apmOptions = new HashMap<>();
        }
        apmOptions.put("paymentMethod", checkoutDetail.getApmPaymentMethod());
        switch (checkoutDetail.getApmPaymentMethod()) {
            case "apmgw_BOLETO":
            case "apmgw_Baloto":
            case "apmgw_BBVA":
            case "apmgw_BBVA_Bancomer":
            case "apmgw_Davivienda":
                apmOptions.put("personal_id", "375-53-XXXX");
                break;
            case "apmgw_EPS":
                apmOptions.put("payer_name", "Mack Starton");
                break;
            default: break;
        }
        paymentOption.setAlternativePaymentMethod(apmOptions);
        return paymentOption;
    }

    private Object prepareRequest(Class<? extends SafechargeRequest> requestType, CheckoutDetail checkoutDetail) {
        String userTokenId = UUID.randomUUID().toString();
        String clientRequestId = UUID.randomUUID().toString();
        DeviceDetails deviceDetails = prepareDeviceDetails();
        InitPaymentThreeD initPaymentThreeD = prepareInitPaymentThreeD();
        InitPaymentCard initPaymentCard = prepareInitPaymentCard(checkoutDetail, initPaymentThreeD);
        InitPaymentPaymentOption initPaymentOption = prepareInitPaymentOption(initPaymentCard);

        Object request = null;

        if (requestType == InitPaymentRequest.class) {
            request = new InitPaymentRequest
                    .Builder()
                    .addMerchantInfo(MERCHANT_INFO)
                    .addUserTokenId(userTokenId)
                    .addAmount(checkoutDetail.getAmount())
                    .addCurrency(checkoutDetail.getCurrency())
                    .addDeviceDetails(deviceDetails)
                    .addUserTokenId(userTokenId)
                    .addClientUniqueId(config.getClientUniqueId())
                    .addClientRequestId(clientRequestId)
                    .addInitPaymentPaymentOption(initPaymentOption)
                    .addSessionToken(checkoutDetail.getSessionToken())
                    .build();
        }

        if (requestType == PaymentRequest.class) {
            request = new PaymentRequest.Builder()
                    .addMerchantInfo(MERCHANT_INFO)
                    .addUserTokenId(userTokenId)
                    .addAmount(checkoutDetail.getAmount())
                    .addCurrency(checkoutDetail.getCurrency())
                    .addDeviceDetails(deviceDetails)
                    .addUserTokenId(userTokenId)
                    .addClientUniqueId(config.getClientUniqueId())
                    .addClientRequestId(clientRequestId)
                    .addPaymentOption(preparePaymentOption(checkoutDetail))
                    .addSessionToken(checkoutDetail.getSessionToken())
                    .build();
        }

        if (requestType == PaymentAPMRequest.class) {
            request = new PaymentRequest.Builder()
                    .addMerchantInfo(MERCHANT_INFO)
                    .addUserTokenId(userTokenId)
                    .addAmount(checkoutDetail.getAmount())
                    .addCurrency(checkoutDetail.getCurrency())
                    .addDeviceDetails(deviceDetails)
                    .addUserDetails(prepareUserDetails(checkoutDetail))
                    .addUserTokenId(userTokenId)
                    .addClientUniqueId(config.getClientUniqueId())
                    .addClientRequestId(clientRequestId)
                    .addPaymentOption(preparePaymentAPMOption(checkoutDetail))
                    .addSessionToken(checkoutDetail.getSessionToken())
                    .build();
        }

        log.info("[SafeCharge] [{}] - [Request] {}", requestType.getName(), request);
        System.out.println(gson.toJson(request));
        return request;
    }

    private void logResponseToConsole(SafechargeResponse response) {
        log.info("[SafeCharge] [{}] - [Response] {}", response.getClass().getName(), response);
        System.out.println(gson.toJson(response));
    }
}
