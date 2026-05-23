package com.vishal.lld.designpattern.creational.builder.v2_telescoping_construtored_approach;

public class TelescopedConstructoredOrder {

    // Mandotory fields
    @SuppressWarnings("unused")
    private String itemName;
    @SuppressWarnings("unused")
    private int quantity;
    @SuppressWarnings("unused")
    private String paymentMethod;

    // optional fields
    @SuppressWarnings("unused")
    private String size;
    @SuppressWarnings("unused")
    private boolean extraCheese;
    @SuppressWarnings("unused")
    private boolean extraSauce;
    @SuppressWarnings("unused")
    private String deliveryAddress;

    @SuppressWarnings("unused")
    private String specialInstructions;

    // mandatory fields only constructor
    public TelescopedConstructoredOrder(String itemName,
            int quantity, String paymentMethod) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
    }

    // all args constructor
    public TelescopedConstructoredOrder(
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

    // field Combination based constructors
    public TelescopedConstructoredOrder(
            String itemName,
            int quantity,
            String paymentMethod,
            String size) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
        this.size = size;
    }

    public TelescopedConstructoredOrder(
            String itemName,
            int quantity,
            String paymentMethod,
            String size,
            String deliveryAddress) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
        this.size = size;
        this.deliveryAddress = deliveryAddress;
    }

    // ... and so on

    /*
     * This is a common attempt to fix the null problem from v1
     * but introduces a new problem — constructor explosion
     * no finite set of constructors can cover all valid field combinations
     * 
     * In order to achive it we try to use most common fieds combinations
     * and create constructor for all such
     * but it causes multiple construtor for a single class
     * the problems is named as "TELESCOPING CONSTRUCTOR"
     */

}
