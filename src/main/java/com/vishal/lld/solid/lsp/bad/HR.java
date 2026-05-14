package com.vishal.lld.solid.lsp.bad;

public class HR extends Employee {

    public HR(String name, double salary, int leaves) {
        super(name, salary, leaves);
    }

    // utility method to approve leave application for employees
    public boolean processLeaveRequest(Employee emp) {
        if (emp.getLeaveCount() > 0) {
            emp.applyLeave();
            System.out.println("Leave approved for emp: " + emp.getName());
            return true;
        }
        return false;
    }

}
