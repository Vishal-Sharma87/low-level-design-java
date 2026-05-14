package com.vishal.lld.solid.lsp.bad;

public class Employee {

    protected String name;
    protected double salary;
    protected int leaves;

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public Employee(String name, double salary, int leaves) {
        this.name = name;
        this.salary = salary;
        this.leaves = leaves;
    }

    public int getLeaveCount() {
        return this.leaves;
    }

    public void applyLeave() {
        this.leaves--;
    }

}
