package com.vishal.lld.solid.lsp.good;

public class LeaveEligiableEmployee extends Employee {

    private int leave;

    public LeaveEligiableEmployee(String name, double salary, int leave){
        super(name, salary);
        if (leave < 0) {
            throw new IllegalArgumentException("Leave cannot be negative");
        }
        this.leave = leave;
    }

    public int getLeave() {
        return leave;
    }

    public void applyLeave(){
        this.leave--;
    }
}
