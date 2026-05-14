package com.vishal.lld.solid.lsp.good;

public class HR extends Employee {

    public HR(String name, double salary) {
        super(name, salary);
    }


    public boolean processLeaveRequest(LeaveEligiableEmployee emp){
        if (emp.getLeave() > 0) {
            emp.applyLeave();
            System.out.println("Leave approved for :" + emp);
            return true;
        }
        return false;
    }

}
