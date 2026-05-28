package com.vishal.lld.designpattern.behavioural.strategy;

import com.vishal.lld.designpattern.behavioural.strategy.interfaces.PaymentStrategy;

    public class PaymentService {
        private PaymentStrategy strategy;

        public PaymentService(PaymentStrategy paymentStrategy) {
            if (paymentStrategy == null)
                throw new IllegalArgumentException("Payment Strategy cannot be null");
            strategy = paymentStrategy;
        }

        public void pay() {
            strategy.pay();
        }
    }
