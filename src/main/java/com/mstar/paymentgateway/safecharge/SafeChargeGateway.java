package com.mstar.paymentgateway.safecharge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mstar.paymentgateway.models.CheckoutViewModel;
import com.safecharge.biz.Safecharge;
import com.safecharge.biz.SafechargeRequestExecutor;
import com.safecharge.exception.SafechargeException;
import com.safecharge.model.Addendums;
import com.safecharge.model.AmountDetails;
import com.safecharge.model.BrowserDetails;
import com.safecharge.model.Card;
import com.safecharge.model.CurrencyConversion;
import com.safecharge.model.DeviceDetails;
import com.safecharge.model.DynamicDescriptor;
import com.safecharge.model.ExternalSchemeDetails;
import com.safecharge.model.InitPaymentCard;
import com.safecharge.model.InitPaymentPaymentOption;
import com.safecharge.model.InitPaymentThreeD;
import com.safecharge.model.Item;
import com.safecharge.model.MerchantDetails;
import com.safecharge.model.MerchantInfo;
import com.safecharge.model.PaymentOption;
import com.safecharge.model.RestApiUserDetails;
import com.safecharge.model.SubMerchant;
import com.safecharge.model.ThreeD;
import com.safecharge.model.UrlDetails;
import com.safecharge.model.UserAddress;
import com.safecharge.model.V2AdditionalParams;
import com.safecharge.request.GetSessionTokenRequest;
import com.safecharge.request.SafechargeRequest;
import com.safecharge.response.InitPaymentResponse;
import com.safecharge.response.PaymentResponse;
import com.safecharge.response.SafechargeResponse;
import com.safecharge.util.APIConstants;
import com.safecharge.util.AddressUtils;
import com.safecharge.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.UUID;

public class SafeChargeGateway {
    public SafeChargeGateway() {}

    public static SafechargeResponse getSessionToken(@NonNull final Map<String, String> sfcKeys)
            throws SafechargeException {
        Safecharge safecharge = new Safecharge();
        String merchantId = sfcKeys.get("merchantId");
        String siteId = sfcKeys.get("siteId");
        String secretKey = sfcKeys.get("secretKey");
        safecharge.initialize(
                merchantId,
                siteId,
                secretKey,
                APIConstants.INTEGRATION_HOST,
                Constants.HashAlgorithm.SHA256);

        SafechargeRequestExecutor requestExecutor = SafechargeRequestExecutor.getInstance();

        MerchantInfo merchantInfo = new MerchantInfo(
                secretKey,
                merchantId,
                siteId,
                APIConstants.INTEGRATION_HOST,
                Constants.HashAlgorithm.SHA256);

        SafechargeRequest safechargeRequest = (SafechargeRequest) GetSessionTokenRequest.builder()
                .addMerchantInfo(merchantInfo)
                .build();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String requestJSON = gson.toJson(safechargeRequest);
        System.out.println("[SafeCharge] - [SessionTokenRequest]");
        System.out.println(requestJSON);

        SafechargeResponse response = requestExecutor.executeRequest(safechargeRequest);
        String responseJSON = gson.toJson(response);
        System.out.println("[SafeCharge] - [SessionTokenResponse]");
        System.out.println(responseJSON);

        return response;
    }

    public static InitPaymentResponse initializePayment(@NonNull final Map<String, String> sfcKeys,
                                                        @NonNull final CheckoutViewModel checkoutViewModel)
            throws SafechargeException {
        String userTokenId = UUID.randomUUID().toString();
        String clientUniqueId = UUID.randomUUID().toString();
        String clientRequestId = UUID.randomUUID().toString();
        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setIpAddress("127.0.0.1");
        InitPaymentThreeD threeD = new InitPaymentThreeD();
        threeD.setMethodNotificationUrl("<methodNotificationURL>");
        InitPaymentCard card = new InitPaymentCard();
        card.setCardNumber(checkoutViewModel.getCardNumber());
        card.setCardHolderName(checkoutViewModel.getCardholder());
        card.setCVV(checkoutViewModel.getCvv());
        card.setExpirationMonth(checkoutViewModel.getExpiryMonth());
        card.setExpirationYear(checkoutViewModel.getExpiryYear());
        card.setThreeD(threeD);
        InitPaymentPaymentOption initPaymentOption = new InitPaymentPaymentOption();
        initPaymentOption.setCard(card);
        Safecharge safecharge = new Safecharge();
        safecharge.initialize(sfcKeys.get("merchantId"),
                sfcKeys.get("siteId"),
                sfcKeys.get("secretKey"),
                APIConstants.INTEGRATION_HOST,
                Constants.HashAlgorithm.SHA256);
        return safecharge.initPayment(
                userTokenId,
                clientUniqueId,
                clientRequestId,
                checkoutViewModel.getCurrency(),
                checkoutViewModel.getAmount(),
                deviceDetails,
                initPaymentOption,
                null,
                null,
                null,
                null);
    }

    public static PaymentResponse doPayment(@NonNull final Map<String, String> sfcKeys,
                                                    @NonNull final CheckoutViewModel checkoutViewModel)
            throws SafechargeException {
        String userTokenId = UUID.randomUUID().toString();
        String clientUniqueId = UUID.randomUUID().toString();
        String clientRequestId = UUID.randomUUID().toString();
        String currency = checkoutViewModel.getCurrency();
        String amount = checkoutViewModel.getAmount();

        V2AdditionalParams v2AdditionalParams = new V2AdditionalParams();
        v2AdditionalParams.setChallengeWindowSize("05");

        BrowserDetails browserDetails = new BrowserDetails();
        browserDetails.setAcceptHeader("text/html,application/xhtml+xml");
        browserDetails.setIp("192.168.1.11");
        browserDetails.setJavaEnabled("TRUE");
        browserDetails.setJavaScriptEnabled("TRUE");
        browserDetails.setLanguage("EN");
        browserDetails.setColorDepth("48");
        browserDetails.setScreenHeight("400");
        browserDetails.setScreenWidth("600");
        browserDetails.setTimeZone("0");
        browserDetails.setUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47)");

        ThreeD threeD = new ThreeD();
        threeD.setMethodCompletionInd("Y");
        threeD.setVersion("2.1.0");
        threeD.setNotificationURL("<notificationURL>");
        threeD.setMerchantURL("<merchantURL>");
        threeD.setV2AdditionalParams(v2AdditionalParams);
        threeD.setBrowserDetails(browserDetails);

        Card card = new Card();
        card.setCardNumber(checkoutViewModel.getCardNumber());
        card.setCardHolderName(checkoutViewModel.getCardholder());
        card.setExpirationMonth(checkoutViewModel.getExpiryMonth());
        card.setExpirationYear(checkoutViewModel.getExpiryYear());
        card.setCVV(checkoutViewModel.getCvv());
        card.setThreeD(threeD);

        PaymentOption paymentOption = new PaymentOption();
        paymentOption.setCard(card);

        UserAddress billingAddress = new UserAddress();
        billingAddress.setCountry(checkoutViewModel.getCountry());
        billingAddress.setEmail(checkoutViewModel.getEmail());

        DeviceDetails deviceDetails = new DeviceDetails();
        deviceDetails.setIpAddress("127.0.0.1");

        Safecharge safecharge = new Safecharge();
        safecharge.initialize(sfcKeys.get("merchantId"),
                sfcKeys.get("siteId"),
                sfcKeys.get("secretKey"),
                APIConstants.INTEGRATION_HOST,
                Constants.HashAlgorithm.SHA256);
        return safecharge.payment(
                userTokenId,
                clientUniqueId,
                clientRequestId,
                paymentOption,
                0,
                currency,
                amount,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
