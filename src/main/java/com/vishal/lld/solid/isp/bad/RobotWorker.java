package com.vishal.lld.solid.isp.bad;

import com.vishal.lld.solid.isp.bad.interfaces.WorkerEmployee;

public class RobotWorker implements WorkerEmployee {

    @Override
    public void work() {
        System.out.println("Robot is Working...");
    }

    @Override
    public void attendMeeting() {
        System.out.println("Robot is attending meeting...");
    }

    // Methods under "WorkerEmployee" interface that are not supported by
    // RobotWorker
    // The "WorkerEmployee" interface is fat and need re-design
    @Override
    public void eat() {
        throw new UnsupportedOperationException("Robot don't eat.");
    }

    @Override
    public void sleep() {
        throw new UnsupportedOperationException("RObot don't sleep.");
    }

    @Override
    public void claimExpenses() {
        throw new UnsupportedOperationException("Robot don't claim expenses");
    }

}
