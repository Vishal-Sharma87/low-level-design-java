public class Car {

    /*
     * 3 fields: brand, color, speed
     * 1 method: displayInfo() that prints all three fields
     * A main method where you create 2 different Car objects with different values
     * and call displayInfo() on both
     */

    // Properties of car
    String brand;
    String color;
    int speed;

    // Behaviours
    void displayInfo() {
        System.out.println("brand: " + brand);
        System.out.println("color: " + color);
        System.out.println("speed: " + speed);
    }

    public static void main(String[] args) {
        Car coupe = new Car();
        Car sedan = new Car();

        // Setting the values for "Car" objects
        coupe.brand = "BMW";
        coupe.color = "Black";
        coupe.speed = 120;

        sedan.brand = "Mercedes";
        sedan.color = "Navy Blue";
        sedan.speed = 90;

        // Displaying the info of each "Car" object
        System.out.println("Displaing info for :" + coupe);
        coupe.displayInfo();

        System.out.println("Displaing info for :" + sedan);
        sedan.displayInfo();
    }

}
