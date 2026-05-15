package com.vishal.lld.solid.isp.bad;

import com.vishal.lld.solid.isp.bad.interfaces.WorkerEmployee;

public class HumenWorker implements WorkerEmployee {

    @Override
    public void work() {
        System.out.println("Humen Working...");
    }

    @Override
    public void attendMeeting() {
        System.out.println("Humen attending meeting...");
    }

    @Override
    public void eat() {
        System.out.println("Humen eating...");
    }

    @Override
    public void sleep() {
        System.out.println("Humen sleeping...");
    }

    @Override
    public void claimExpenses() {
        System.out.println("Humen's Expenses Claimed.");
    }

}
