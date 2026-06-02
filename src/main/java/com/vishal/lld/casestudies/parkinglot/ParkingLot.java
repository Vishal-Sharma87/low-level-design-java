package com.vishal.lld.casestudies.parkinglot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vishal.lld.casestudies.parkinglot.enums.VehicleType;
import com.vishal.lld.casestudies.parkinglot.factory.PaymentStrategyFactory;
import com.vishal.lld.casestudies.parkinglot.models.ParkingTicket;
import com.vishal.lld.casestudies.parkinglot.models.Slot;
import com.vishal.lld.casestudies.parkinglot.models.Vehicle;

public class ParkingLot {
    private static volatile ParkingLot instance;

    private Map<Integer, ParkingTicket> parkingTickets;
    private SlotManager slotManager;
    private BillingSystem billingSystem;

    private ParkingLot() {
        this.parkingTickets = new ConcurrentHashMap<>();
        slotManager = SlotManager.getInstance();
    }

    public static ParkingLot getInstance(List<VehicleType> vehicleTypes, int totalSlots, BillingSystem billingSystem) {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot();
                    instance.billingSystem = billingSystem;
                    vehicleTypes.forEach(type -> {
                        for (int i = 0; i < totalSlots; i++) {
                            Slot slot = new Slot(type);
                            instance.slotManager.populateUnocuppiedSlot(slot.getVehicleType(), slot.getSlotId());
                        }
                    });
                }
            }
        }
        return instance;
    }

    // park
    /*
     * get available slot
     * if found create parking ticket
     * return the ticket id (slotID)
     */

    public int park(Vehicle vehicle) throws RuntimeException {
        if (vehicle == null)
            throw new IllegalArgumentException("Vehicle cannot be null");

        Integer availableSlotId = slotManager.getAvailableSlot(vehicle.getVehicleType());

        ParkingTicket ticket = new ParkingTicket(availableSlotId, vehicle.getVehicleId(), vehicle.getVehicleType());
        parkingTickets.put(availableSlotId, ticket);

        System.out.printf("ParkingTicket created for vehicleId: %d at slotId: %d\n", vehicle.getVehicleId(),
                availableSlotId);

        return availableSlotId;
    }

    public void exit(int slotId, String paymentType) {
        if (!parkingTickets.containsKey(slotId)) {
            throw new RuntimeException("Invalid slotId: " + slotId);
        }

        try {
            ParkingTicket ticket = parkingTickets.get(slotId);

            billingSystem.processBilling(PaymentStrategyFactory.getPaymentStrategy(paymentType), ticket);

            slotManager.freeSlot(ticket.getVehicleType(), slotId);

            parkingTickets.remove(slotId);
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println(illegalArgumentException.getMessage());
        } catch (Exception exception) {
            System.out.println("Something went wrong, message: " + exception.getMessage());
        }
    }
}
