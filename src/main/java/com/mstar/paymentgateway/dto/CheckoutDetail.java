package com.mstar.paymentgateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class CheckoutDetail {
    private String amount;
    private String currency;
    private String country;
    private String cardholder;
    private String cardNumber;
    private String cvv;
    private String expiryMonth;
    private String expiryYear;
    private String email;
    private String phone;
    private String sessionToken;
    private String apmPaymentMethod;
    private Map<String, String> apmOptions;
    private Map<Integer, String> apmList = getApmMap();

    private Map<Integer, String> getApmMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "apmgw_2C2P_APMs");
        map.put(2, "apmgw_Abon");
        map.put(3, "apmgw_Afterpay");
        map.put(4, "apmgw_Aircash");
        map.put(5, "apmgw_Airtel_Money");
        map.put(6, "apmgw_AliPay_DI");
        map.put(7, "apmgw_Alipay");
        map.put(8, "apmgw_AlipayHK");
        map.put(9, "apmgw_ApmEmulator");
        map.put(10, "apmgw_AstroPay");
        map.put(11, "apmgw_AstropayBankPayouts");
        map.put(12, "apmgw_AstropayPrePaid");
        map.put(13, "apmgw_Astropay_TEF");
        map.put(14, "apmgw_BBVA");
        map.put(15, "apmgw_BBVA_Bancomer");
        map.put(16, "apmgw_BIIVA");
        map.put(17, "apmgw_BOLETO");
        map.put(18, "apmgw_Baloto");
        map.put(19, "apmgw_BanContact");
        map.put(20, "apmgw_Bangkok_Bank_ATM");
        map.put(21, "apmgw_Bangkok_Bank_Mobile");
        map.put(22, "apmgw_Bangkok_Bank_iPay");
        map.put(23, "apmgw_Boku");
        map.put(24, "apmgw_CASHlib_");
        map.put(25, "apmgw_CIMBClicks");
        map.put(26, "apmgw_Citadel");
        map.put(27, "apmgw_Davivienda");
        map.put(28, "apmgw_DragonPay");
        map.put(29, "apmgw_EFT_PROD");
        map.put(30, "apmgw_EPS");
        map.put(31, "apmgw_EasyEFT");
        map.put(32, "apmgw_Efecty");
        map.put(33, "apmgw_Elo");
        map.put(34, "apmgw_Finnish_Banks");
        map.put(35, "apmgw_Fonix");
        map.put(36, "apmgw_Giropay");
        map.put(37, "apmgw_GoPay");
        map.put(38, "apmgw_HiPay");
        map.put(39, "apmgw_Hipercard");
        map.put(40, "apmgw_IDS");
        map.put(41, "apmgw_INTERAC_Instant");
        map.put(42, "apmgw_InstaDebit");
        map.put(43, "apmgw_Interac");
        map.put(44, "apmgw_Interac_Combined");
        map.put(45, "apmgw_Interac_eTransfer");
        map.put(46, "apmgw_Loterica");
        map.put(47, "apmgw_MBWAY");
        map.put(48, "apmgw_MISTERCASH");
        map.put(49, "apmgw_MTN_Mobile_Money");
        map.put(50, "apmgw_MULTIBANCO");
        map.put(51, "apmgw_M_Pesa");
        map.put(52, "apmgw_Mandiri");
        map.put(53, "apmgw_MercadoPago");
        map.put(54, "apmgw_Moneta__Direct_Integration_");
        map.put(55, "apmgw_MoneyBookers");
        map.put(56, "apmgw_MuchBetter");
        map.put(57, "apmgw_MyBank");
        map.put(58, "apmgw_NeoSurf_Direct_Integration_");
        map.put(59, "apmgw_Neosurf");
        map.put(60, "apmgw_Neteller");
        map.put(61, "apmgw_OXXO_PAY");
        map.put(62, "apmgw_Open_Banking");
        map.put(63, "apmgw_Oxxo");
        map.put(64, "apmgw_P24");
        map.put(65, "apmgw_PIX");
        map.put(66, "apmgw_POLI");
        map.put(67, "apmgw_POLINZ");
        map.put(68, "apmgw_PSE");
        map.put(69, "apmgw_PSE_PZ");
        map.put(70, "apmgw_Pay4Fun");
        map.put(71, "apmgw_Pay4Me");
        map.put(72, "apmgw_PayRetailers");
        map.put(73, "apmgw_PayRetailers_CC");
        map.put(74, "apmgw_PayRetailers_Payouts");
        map.put(75, "apmgw_PaySafeCard");
        map.put(76, "apmgw_PaySafeCash");
        map.put(77, "apmgw_PayU");
        map.put(78, "apmgw_PayWithCrypto");
        map.put(79, "apmgw_Paysera");
        map.put(80, "apmgw_PesaLink");
        map.put(81, "apmgw_PlayPlus");
        map.put(82, "apmgw_PostFinance_Card");
        map.put(83, "apmgw_PostFinance_eFinance");
        map.put(84, "apmgw_SEPA_Payouts");
        map.put(85, "apmgw_Safetypay");
        map.put(86, "apmgw_Santander");
        map.put(87, "apmgw_Sepa");
        map.put(88, "apmgw_Skrill_RapidTransfer");
        map.put(89, "apmgw_Sofort");
        map.put(90, "apmgw_Su_Red__OTHERS_CASH_");
        map.put(91, "apmgw_SugarPay");
        map.put(92, "apmgw_TWINT");
        map.put(93, "apmgw_Tigo");
        map.put(94, "apmgw_Tingg_Wallet");
        map.put(95, "apmgw_Tpay");
        map.put(96, "apmgw_Trustly");
        map.put(97, "apmgw_Trustly_DI");
        map.put(98, "apmgw_UnionPay");
        map.put(99, "apmgw_VIP_Preferred");
        map.put(100, "apmgw_WeChat");
        map.put(101, "apmgw_WebMoney");
        map.put(102, "apmgw_eCheckSelect");
        map.put(103, "apmgw_ecoPayz");
        map.put(104, "apmgw_expresscheckout");
        map.put(105, "apmgw_iDeal");
        map.put(106, "apmgw_iDebit");
        return map;
    }
}