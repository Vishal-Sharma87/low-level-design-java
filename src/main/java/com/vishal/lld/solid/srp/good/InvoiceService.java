package com.vishal.lld.solid.srp.good;

import java.util.List;

public class InvoiceService {

    /*
     * Orchestrator -> Communicates with all, but has no business logic
     * The class owns logic of communication between two needy services
     * one reason to change -> communaication logic shifts from A to B service
     */

    // Classes that owns logic
    private InvoiceCalculator calculator = new InvoiceCalculator();
    private InvoiceRepository repository = new InvoiceRepository();
    private InvoicePresentation presentation = new InvoicePresentation();

    // calls InvoiceCalculator -> owns no logic
    public int calculateTotalPrice(List<String> items) {
        return calculator.calculateTotal(items);
    }

    public int getDiscountedPrice(int amount) {
        return calculator.applyDiscount(amount);
    }

    // calls repository -> DB layer -> owns no logic
    public void saveInDatabase(Object invoice) {
        repository.saveInvoice(invoice);
    }

    // calls presentation -> owns no logic
    public String getPDF(Object invoice) {
        return presentation.generatePdf(invoice);
    }

}
