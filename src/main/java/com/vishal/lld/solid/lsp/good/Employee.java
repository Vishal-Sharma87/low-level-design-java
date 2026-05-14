package com.vishal.lld.solid.lsp.good;

public class Employee {
    public String name;
    public double salary;

    public Employee(String name, double salary) {
        if (salary < 0)
            throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
        this.name = name;
    }
}
