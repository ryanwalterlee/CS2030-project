package cs2030.simulator;

import java.util.HashMap;

class Stats {
    
    private final HashMap<String, Double> map;

    Stats() {
        this.map = new HashMap<>();
        this.map.put("totalWaitingTime", 0.0);
        this.map.put("numHumanServed", 0.0);
        this.map.put("numSelfCheckout", 0.0);
        this.map.put("totalLeave", 0.0);
    }

    void addWaitingTime(double time) {
        double currWaitingTime = this.map.get("totalWaitingTime");
        this.map.replace("totalWaitingTime", currWaitingTime + time);
    }

    void addHumanServed() {
        double currHumanServed = this.map.get("numHumanServed");
        this.map.replace("numHumanServed", currHumanServed + 1);
    }

    int getHumanServed() {
        double numHumanServed = this.map.get("numHumanServed");
        return (int) numHumanServed;
    }

    void addScoServed() {
        double currScoServed = this.map.get("numSelfCheckout");
        this.map.replace("numSelfCheckout", currScoServed + 1);
    }

    int getNumServed() {
        double humanServed = this.map.get("numHumanServed");
        double scoServed = this.map.get("numSelfCheckout");
        return (int) (humanServed + scoServed);
    }

    void addTotalLeave() {
        double currTotalLeave = this.map.get("totalLeave");
        this.map.replace("totalLeave", currTotalLeave + 1);
    }

    void printStats() {
        double totalWaitingTime = this.map.get("totalWaitingTime");
        double numServed = this.map.get("numHumanServed") + this.map.get("numSelfCheckout");
        double totalLeave = this.map.get("totalLeave");
        double averageWaitingTime = totalWaitingTime / numServed;
        System.out.println("[" + String.format("%.3f", averageWaitingTime) 
                + " " + (int) numServed + " " + (int) totalLeave + "]");
    }
    
}
