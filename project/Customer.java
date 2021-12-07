package cs2030.simulator;

import java.util.function.Supplier;

class Customer {
    private final int identifier;
    private final double servingTime;
    private final Supplier<Double> lazyServingTime;
    private final double arrivalTime;
    private final boolean isGreedy;

    Customer(int identifier, double servingTime, double arrivalTime) {
        this.identifier = identifier;
        this.servingTime = servingTime;
        this.arrivalTime = arrivalTime;
        this.lazyServingTime = () -> 0.0;
        this.isGreedy = false;
    }

    Customer(int identifier, 
        Supplier<Double> lazyServingTime, 
        double arrivalTime, 
        boolean isGreedy) {

        this.identifier = identifier;
        this.lazyServingTime = lazyServingTime;
        this.arrivalTime = arrivalTime;
        this.servingTime = -1;
        this.isGreedy = isGreedy;
    }

    double getServingTime() {
        if (servingTime == -1) {
            return lazyServingTime.get();
        } else {
            return servingTime;
        }
    }

    double getArrivalTime() {
        return arrivalTime;
    }

    int getIdentifier() {
        return identifier;
    }

    boolean isGreedy() {
        return isGreedy;
    }
}
