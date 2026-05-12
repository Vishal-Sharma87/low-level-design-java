package com.vishal.lld.solid.srp.good;

import java.util.List;

public class InvoiceCalculator {

    // business logic -> change by Finance Team
    public int calculateTotal(List<String> items) {
        // simulating a calculation
        return items.size() * 10;
    }

    public int applyDiscount(int amount) {
        // 10% discount
        int discount = amount / 10;

        return amount - discount;
    }

}
