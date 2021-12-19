package com.mstar.paymentgateway.controller;

import com.mstar.paymentgateway.dto.CheckoutDetail;
import com.mstar.paymentgateway.service.safecharge.SafeChargeService;
import com.safecharge.response.GetSessionTokenResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(path = "/")
public class PageController {

    private final SafeChargeService safeChargeService;

    public PageController(SafeChargeService safeChargeService) {
        this.safeChargeService = safeChargeService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping(path = "/checkout")
    public ModelAndView checkout(@RequestParam Map<String, String> params) {
        GetSessionTokenResponse sessionToken = safeChargeService.getSessionToken();
        ModelAndView mav = new ModelAndView("checkout");
        CheckoutDetail checkoutViewModel = new CheckoutDetail();
        checkoutViewModel.setSessionToken(sessionToken.getSessionToken());
        checkoutViewModel.setAmount(params.get("amount"));
        checkoutViewModel.setCurrency(params.get("currency"));
        mav.addObject("checkoutViewModel", checkoutViewModel);
        return mav;
    }
}
