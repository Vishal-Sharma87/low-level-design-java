package com.vishal.lld.solid.ocp.bad;

public class PaymentService {

    // Decides which option to call and performa that calling
    // Deciding of payment is not dependent on actual business logic
    // The logic is to process the payment no matter which option is given
    
    public void processPayment(String type, double amount) {

        if (type.equals("UPI")) {
            System.out.println("Processing with UPI payement of amount: " + String.valueOf(amount));
            // call to method that handles UPI payments
        } else if (type.equals("CARD")) {
            System.out.println("Processing with CARD payement of amount: " + String.valueOf(amount));
            // call to method that handles CARD payments
        }

        // tommorrow: add NETBANKING -> add another "else-if" block
        // next week: add CRYPTO -> add another "else if" block and so on

        /*
         * Here every new option adds an else-if bock
         * one typo or missing piece and break existing flow
         */
    }

}
