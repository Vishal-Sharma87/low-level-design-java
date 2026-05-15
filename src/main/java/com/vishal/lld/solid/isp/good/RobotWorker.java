package com.vishal.lld.solid.isp.good;

import com.vishal.lld.solid.isp.good.interfaces.Workable;

public class RobotWorker implements Workable {

    @Override
    public void work() {
        System.out.println("Robot is working...");
    }

    @Override
    public void attendMeeting() {
        System.out.println("Robot is attending meeting...");
    }

}
