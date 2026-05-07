public class Car {

    // Properties of car
    private String brand;
    private String color;
    private int speed;

    // constructor
    public Car(String brand, String color, int speed) {
        this.brand = brand;
        this.color = color;
        if (speed < 0)
            throw new IllegalArgumentException("Object creation with negative speed is not allowed");
        this.speed = speed;
    }

    // getters and setters
    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSpeed(int speed) {
        if (speed < 0) {
            System.out.println("Warning: Speed set fail, speed cannot be negative");
            return;
        }
        this.speed = speed;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Behaviours
    void displayInfo() {
        System.out.println("brand: " + brand);
        System.out.println("color: " + color);
        System.out.println("speed: " + speed);
    }

    public static void main(String[] args) {
        Car coupe = new Car("BMW", "Black", 120);
        Car sedan = new Car("Mercedes", "Navy Blue", 90);

        // Displaying the info of each "Car" object
        System.out.println("Displaing info for :" + coupe);
        coupe.displayInfo();

        System.out.println("Displaing info for :" + sedan);
        sedan.displayInfo();

        // trying to set negative speed -> Assuming warning
        coupe.setSpeed(-66);
    }

}
