package com.mstar.paymentgateway.service.payoneer;

import com.mstar.paymentgateway.api.request.payoneer.GetListURLRequest;

public interface PayoneerService {
    String getListURL(GetListURLRequest request);
}
