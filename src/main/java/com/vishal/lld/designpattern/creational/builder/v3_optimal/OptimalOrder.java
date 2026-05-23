package com.vishal.lld.designpattern.creational.builder.v3_optimal;

public class OptimalOrder {

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

    /*
     * CORE IDEA
     * Do not expose any public constructor keep one private constructor so that
     * class itself not let users create independently they must use "something".
     * 
     * Object creation logic must enforce fully functional or No creation at all
     * 
     * Keep a private static "builder" class that will whole class fields together
     * 
     * and use the above created private constructor
     */

    private OptimalOrder(Builder builder) {
        this.itemName = builder.itemName;
        this.quantity = builder.quantity;
        this.paymentMethod = builder.paymentMethod;
        this.size = builder.size;
        this.extraCheese = builder.extraCheese;
        this.extraSauce = builder.extraSauce;
        this.deliveryAddress = builder.deliveryAddress;
        this.specialInstructions = builder.specialInstructions;
    }

    public static class Builder {

        private String itemName;
        private int quantity;
        private String paymentMethod;

        // optional fields
        private String size;
        private boolean extraCheese;
        private boolean extraSauce;
        private String deliveryAddress;
        private String specialInstructions;

        // mandatory fielded Builder constructor
        private Builder(String itemName, int quantity, String paymentMethod) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.paymentMethod = paymentMethod;
        }

        // setting optional fields only if asked

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder extraCheese(boolean extraCheese) {
            this.extraCheese = extraCheese;
            return this;
        }

        public Builder extraSauce(boolean extraSauce) {
            this.extraSauce = extraSauce;
            return this;
        }

        public Builder deliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Builder specialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
            return this;
        }

        // method to create the "OptimalOrder" object
        public OptimalOrder build() {
            // pre object creation edge case check
            if (this.itemName == null || this.itemName.isEmpty())
                throw new IllegalStateException("itemName cannot be empty");
            if (this.quantity <= 0)
                throw new IllegalStateException("Quantity Must be greater than 0");

            return new OptimalOrder(this);
        }

    }

    public static Builder builder(String itemName, int quantity, String paymentMethod) {
        return new Builder(itemName, quantity, paymentMethod);
    }

}
