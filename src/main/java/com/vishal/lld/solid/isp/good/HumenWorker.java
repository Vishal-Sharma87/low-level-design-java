package com.vishal.lld.solid.isp.good;

import com.vishal.lld.solid.isp.good.interfaces.Biological;
import com.vishal.lld.solid.isp.good.interfaces.Claimable;
import com.vishal.lld.solid.isp.good.interfaces.Workable;

public class HumenWorker implements Workable, Biological, Claimable {

    @Override
    public void claimExpenses() {
        System.out.println("Humen's expenses claimed.");
    }

    @Override
    public void eat() {
        System.out.println("Humen is eating...");
    }

    @Override
    public void sleep() {
        System.out.println("Humen is sleeping...");
    }

    @Override
    public void work() {
        System.out.println("Humen is working...");
    }

    @Override
    public void attendMeeting() {
        System.out.println("Humen is attending meeting...");
    }

}
