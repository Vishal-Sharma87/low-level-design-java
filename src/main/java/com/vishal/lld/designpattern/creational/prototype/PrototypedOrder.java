package com.vishal.lld.designpattern.creational.prototype;

public class PrototypedOrder {

    /*
     * This pattern allows us to create Order object from an existing Order object,
     * it is usefull when we want a minor change from an existing oredr like just
     * want to update payment type and rest of the fields remain unchanged
     */

    private String itemName;
    private int quantity;
    private String paymentMethod;

    private String size;
    private boolean extraCheese;
    private String address;
    private String specialInstructions;

    private PrototypedOrder(Builder builder) {
        this.itemName = builder.itemName;
        this.quantity = builder.quantity;
        this.paymentMethod = builder.paymentMethod;
        this.size = builder.size;
        this.extraCheese = builder.extraCheese;
        this.address = builder.address;
        this.specialInstructions = builder.specialInstructions;
    }

    public static class Builder {
        private String itemName;
        private int quantity;
        private String paymentMethod;

        private String size;
        private boolean extraCheese;
        private String address;
        private String specialInstructions;

        // No args constructor for "from" method
        private Builder() {
        }

        // constructor enforces mandatory fields
        private Builder(String itemName, int quantity, String paymentMethod) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.paymentMethod = paymentMethod;
        }

        // prototyped version method "from" existing "Order" object
        private static Builder from(PrototypedOrder order) {
            Builder builder = new Builder();

            builder.itemName = order.itemName;
            builder.quantity = order.quantity;
            builder.paymentMethod = order.paymentMethod;
            builder.size = order.size;
            builder.extraCheese = order.extraCheese;
            builder.address = order.address;
            builder.specialInstructions = order.specialInstructions;

            return builder;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder extraCheese(boolean extraCheese) {
            this.extraCheese = extraCheese;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder specialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
            return this;
        }

        public PrototypedOrder build() {
            if (this.itemName == null || this.itemName.isEmpty())
                throw new IllegalStateException("itemName cannot be empty");
            if (this.quantity <= 0)
                throw new IllegalStateException("Quantity Must be greater than 0");

            return new PrototypedOrder(this);
        }
    }

    public static Builder builder(String itemName, int quantity, String paymentMethod) {
        return new Builder(itemName, quantity, paymentMethod);
    }

    public static Builder from(PrototypedOrder order) {
        return Builder.from(order);
    }

}
