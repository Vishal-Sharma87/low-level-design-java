package com.vishal.lld.solid.ocp.good.factory;

import com.vishal.lld.solid.ocp.good.interfaces.PaymentStrategy;
import com.vishal.lld.solid.ocp.good.paymentoptions.CardPayment;
import com.vishal.lld.solid.ocp.good.paymentoptions.NetbankingPayment;
import com.vishal.lld.solid.ocp.good.paymentoptions.UpiPayment;

public class PaymentStrategyFactory {

    /*
     * Isolated class return PaymentStrategy associated with given type
     */

    public PaymentStrategy getPaymentStrategy(String type) {
        return switch (type) {
            case "UPI" -> new UpiPayment();
            case "CARD" -> new CardPayment();
            case "NETBANKING" -> new NetbankingPayment();
            default -> throw new IllegalArgumentException("Unknown Payment type");
        };
    }
}
