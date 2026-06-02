package com.vishal.lld.casestudies.parkinglot;

import com.vishal.lld.casestudies.parkinglot.interfaces.PaymentStrategy;
import com.vishal.lld.casestudies.parkinglot.models.ParkingTicket;

public class BillingSystem {

    public void processBilling(PaymentStrategy paymentStrategy, ParkingTicket ticket) {
        System.out.printf("Processing billing for slotId: %d with vehicleId: %d\n", ticket.getSlotId(),
                ticket.getVehicleId());

        paymentStrategy.pay();

        System.out.printf("Payment successfull for slotId: %d with vehicleId: %d\n", ticket.getSlotId(),
                ticket.getVehicleId());
    }
}
