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

    // constructor
    public Car(String brand, String color, int speed) {
        this.brand = brand;
        this.color = color;
        this.speed = speed;
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
    }

    /*
     * Interview Questions — Constructors & this
     * Q1. What is the difference between a constructor and a method?
     * Ans1:
     * Constructor -> special type of method, invoke once at the object creation
     * no return type
     * used to initialize object fields
     * 
     * Method -> reusable block of code belongs to object itself.
     * used to do some task with object fields
     * have return type .
     * can be invoke multiple times.
     * 
     * Q2. Java automatically provides a constructor if you don't write one — what
     * is it called and what does it do? What happens when you define your own
     * constructor?
     * Ans2:
     * the constructor provide by Java is called Default Constructor that have not
     * parameters,
     * when we define one constructor by ourself Java overrides that default with
     * our constructor.
     * 
     * The moment you define your own constructor, Java removes the default no-arg
     * constructor. new Car() no longer works unless you explicitly write a no-arg
     * constructor yourself.
     * 
     * Q3. Can a constructor have a return type?
     * Ans3: No, constructor can't have any return type or else there will be no
     * difference between a constructor or method. But do not know the exact reason
     * behind it.
     * 
     * Q4. What is constructor overloading?
     * Ans4: Overloading is compile time polymorphism property, it means defining
     * multiple constructors but the condition is number of parameters or relative
     * ordering of parameters must be different,
     * Java will itself call the best suitable on the basis of what object creation
     * demands.
     * 
     * 
     * Q5. What are the uses of the this keyword in Java? (There are more than one —
     * think beyond just field assignment)
     * Ans5: "this" keywords holds the* reference of the object which calls the
     * method.
     * used to point object fields
     * this() — call one constructor from another constructor (constructor chaining)
     * this as argument — pass the current object to another method:
     * someMethod(this)
     */

}
