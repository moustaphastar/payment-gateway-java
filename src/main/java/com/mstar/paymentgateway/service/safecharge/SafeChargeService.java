package com.mstar.paymentgateway.service.safecharge;

import com.mstar.paymentgateway.dto.CheckoutDetail;
import com.safecharge.response.GetSessionTokenResponse;
import com.safecharge.response.InitPaymentResponse;
import com.safecharge.response.PaymentAPMResponse;
import com.safecharge.response.PaymentResponse;

public interface SafeChargeService {
    //OpenOrderResponse openOrder();
    GetSessionTokenResponse getSessionToken();
    InitPaymentResponse initPayment(CheckoutDetail checkoutDetail);
    PaymentResponse executePayment(CheckoutDetail checkoutDetail);
    PaymentResponse executePaymentAPM(CheckoutDetail checkoutDetail);

}
