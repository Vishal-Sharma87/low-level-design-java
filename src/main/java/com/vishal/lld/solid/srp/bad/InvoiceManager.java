package com.vishal.lld.solid.srp.bad;

import java.util.ArrayList;
import java.util.List;

public class InvoiceManager {

    /*
     * THE GOD CLASS -> Three reasons to change
     * 
     * Finance Team -> change in business logic
     * Backend Team -> change DB from MySQl to PostfreSQL or other
     * Design Team -> Change formatting or visual items for PDF
     * 
     * 
     * Violates SRP principle
     * "There should be one reason to change a class"
     */

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

    // persistence -> change by DBA/ backend team
    public void saveInvoice(Object invoice) {
        // simulatig db processing
        List<Object> db = new ArrayList<>();

        db.add(invoice);
    }

    // presentation -> change by Design Team
    public String generatePdf(Object invoice) {
        // simulationg PDF generation
        return String.valueOf(invoice);
    }

}
