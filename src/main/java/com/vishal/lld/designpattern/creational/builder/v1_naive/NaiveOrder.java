package com.vishal.lld.designpattern.creational.builder.v1_naive;

public class NaiveOrder {
    @SuppressWarnings("unused")
    private String itemName;
    @SuppressWarnings("unused")
    private int quantity;
    @SuppressWarnings("unused")
    private String size;
    @SuppressWarnings("unused")
    private boolean extraCheese;
    @SuppressWarnings("unused")
    private boolean extraSauce;
    @SuppressWarnings("unused")
    private String deliveryAddress;
    @SuppressWarnings("unused")
    private String paymentMethod;
    @SuppressWarnings("unused")
    private String specialInstructions;

    // do every Order needs this much parameters?
    // the short answer is NO,
    // Not all orders wants extraCheese or extraSouce
    // Not all orders are deleverable some are dine-in or pickup
    // Not all orders have specialInstructions
    // Such optional fields must only be set if they asked
    public NaiveOrder(
            String itemName,
            int quantity,
            String size,
            boolean extraCheese,
            boolean extraSauce,
            String deliveryAddress,
            String paymentMethod,
            String specialInstructions) {

        this.itemName = itemName;
        this.quantity = quantity;
        this.size = size;
        this.extraCheese = extraCheese;
        this.extraSauce = extraSauce;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.specialInstructions = specialInstructions;
    }
}
