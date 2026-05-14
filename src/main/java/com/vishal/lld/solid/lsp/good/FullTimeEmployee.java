package com.vishal.lld.solid.lsp.good;

public class FullTimeEmployee extends LeaveEligiableEmployee{

    public FullTimeEmployee(String name, double salary, int leave) {
        super(name, salary, leave);
    }


    public int getLeave() {
        return super.getLeave();
    }

    public void applyLeave(){
        super.applyLeave();
    }

}
