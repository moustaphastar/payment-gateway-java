package com.mstar.paymentgateway.controller;

import com.mstar.paymentgateway.models.CheckoutViewModel;
import com.mstar.paymentgateway.payoneer.PayoneerGateway;
import com.safecharge.exception.SafechargeException;
import com.safecharge.request.PaymentRequest;
import com.safecharge.response.InitPaymentResponse;
import com.safecharge.response.PaymentResponse;
import com.safecharge.response.SafechargeResponse;
import com.safecharge.response.ThreeDResponse;
import com.safecharge.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.mstar.paymentgateway.safecharge.SafeChargeGateway.doPayment;
import static com.mstar.paymentgateway.safecharge.SafeChargeGateway.getSessionToken;
import static com.mstar.paymentgateway.safecharge.SafeChargeGateway.initializePayment;

@Controller
@RequestMapping("/")
public class PaymentGatewayController {
    /**
     * SafeCharge api valuesProperties
     */
    @Value("#{${safeCharge}}")
    private final Map<String,String> sfcProps = new HashMap<>();

    /**
     * Payoneer api valuesProperties
     */
    @Value("#{${payoneer}}")
    private final Map<String,String> payoneerProps = new HashMap<>();

    /**
     * Index action.
     * @return Redirects to checkout action.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    /**
     * Get listUrl action for Payoneer.
     * @return listUrl.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/listUrl", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String listUrl() {
        String response;
        try {
            response = PayoneerGateway.getListUrl(payoneerProps,"300");
            return response;
        } catch (IOException exception) {
            return null;
        }
    }

    /**
     * Checkout action.
     * @return String representation of checkout page.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/checkout")
    public ModelAndView checkout(@RequestParam Map<String, String> params) throws SafechargeException {
        SafechargeResponse sessionToken = getSessionToken(sfcProps);
        ModelAndView mav = new ModelAndView("checkout");
        CheckoutViewModel checkoutViewModel = new CheckoutViewModel();
        checkoutViewModel.setId(sessionToken.getSessionToken());
        checkoutViewModel.setAmount(params.get("amount"));
        checkoutViewModel.setCurrency(params.get("currency"));
        mav.addObject("checkoutViewModel", checkoutViewModel);
        return mav;
    }

    /**
     * Processes payment request and returns the Safecharge api result.
     * @param checkoutViewModel Form model object for checkout page.
     * @return Success view if transaction succeeds, otherwise failure view.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/initPayment")
    public ModelAndView initPayment(
            @ModelAttribute CheckoutViewModel checkoutViewModel,
            Model model) {
        ModelAndView mav = new ModelAndView("redirect:success");
        try {
            InitPaymentResponse response = initializePayment(sfcProps, checkoutViewModel);
            if (response.getStatus() == Constants.APIResponseStatus.ERROR
                    && response.getErrCode() > 0) {
                mav.setViewName("redirect:failure");
                mav.addObject("modelHasErrors", false);
                mav.addObject("ApiResponse", response);
                return mav;
            }

            ThreeDResponse threeD = response.getPaymentOption().getCard().getThreeD();
            if (threeD.getV2supported().equals("false") || threeD.getMethodUrl().isEmpty()) {
                System.out.println("Fingerprinting is not needed!");
                System.out.println("Proceeding to first payment request");
                PaymentResponse paymentResponse = doPayment(sfcProps, checkoutViewModel);

                if (paymentResponse.getTransactionStatus().equals("APPROVED")) {
                    System.out.println("Transaction APPROVED! Flow ended.");
                    model.addAttribute("transactionResult", paymentResponse);
                    return new ModelAndView("redirect:/success", "transactionResult" ,model);
                }
            }
            mav.addObject("modelHasErrors", false);
            mav.addObject("ApiResponse", response);
            return mav;
        } catch (SafechargeException exception) {
            mav.setViewName("redirect:failure");
            mav.addObject("message", exception.getMessage());
            return mav;
        }
    }

    /**
     * Processes payment request and returns the Safecharge api result.
     * @param checkoutViewModel Form model object for checkout page.
     * @return Success view if transaction succeeds, otherwise failure view.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/processPayment")
    public ModelAndView processPayment(CheckoutViewModel checkoutViewModel) {
        ModelAndView mav = new ModelAndView("redirect:success");
        try {
            PaymentResponse response = doPayment(sfcProps, checkoutViewModel);
            // If request failed then return failure view.
            if (response.getStatus() == Constants.APIResponseStatus.ERROR
                    && response.getErrCode() > 0) {
                mav.setViewName("redirect:failure");
                mav.addObject("modelHasErrors", false);
                mav.addObject("ApiResponse", response);
                return mav;
            }

            // If the transaction is approved then return success view.
            if (response.getTransactionStatus().equals("APPROVED")) {
                mav.addObject("modelHasErrors", false);
                mav.addObject("ApiResponse", response);
                return mav;
            }

            // If the transaction is redirected then return challenge view.
            if (response.getTransactionStatus().equals("REDIRECT")) {
                return null;
            }

            // If the transaction declined then return failure view.
            if (response.getTransactionStatus().equals("DECLINED")) {
                return null;
            }

            // If the transaction produced error (fallback to v1)
            // proceed Non-3DS transaction payment, without liability shift.
            if (response.getTransactionStatus().equals("ERROR")) {
                return null;
            }
        } catch (SafechargeException exception) {
            mav.setViewName("redirect:failure");
            mav.addObject("message", exception.getMessage());
            return mav;
        }
        return mav;
    }

    /**
     * Success action.
     * The page the client will be redirected after a successful transaction.
     * @return String representation of success page.
     */
    @RequestMapping(method = RequestMethod.GET, path = "success")
    public ModelAndView success(@ModelAttribute Object transactionResult) {
        ModelAndView mav = new ModelAndView("success");
        mav.addObject("apiResponse", transactionResult);
        return mav;
    }

    /**
     * checkout action.
     * The page the client will be redirected after a failed transaction.
     * @return String representation of error page.
     */
    @RequestMapping(method = RequestMethod.GET, path = "failure")
    public String failure(Model model) {
        model.getAttribute("message");
        return "failure";
    }
}
