package com.vishal.lld.solid.srp.good;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {

    // persistence -> change by DBA/ backend team
    public void saveInvoice(Object invoice) {
        // simulatig db processing
        List<Object> db = new ArrayList<>();

        db.add(invoice);
    }

}
