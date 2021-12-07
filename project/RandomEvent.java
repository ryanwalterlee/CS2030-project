package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Optional;

class RandomEvent {
    
    private final Customer customer;
    private final Server server;
    private final double eventTime;
    private final String eventType;
    private final double restTime;

    RandomEvent(String eventType, Customer customer, double time) {
        this.customer = customer;
        this.eventTime = time;
        this.eventType = eventType;
        this.server = new HumanServer(0, 0);
        this.restTime = 0.0;
    }

    RandomEvent(String eventType, 
        Customer customer, 
        Server server, 
        double time) {
        this.customer = customer;
        this.eventTime = time;
        this.eventType = eventType;
        this.server = server;
        this.restTime = 0.0;
    }

    RandomEvent(String eventType, 
        Customer customer, 
        Server server, 
        double time, 
        double restTime) {
        this.customer = customer;
        this.eventTime = time;
        this.eventType = eventType;
        this.server = server;
        this.restTime = restTime;
    }


    double compare(RandomEvent otherEvent) {
        return this.getEventTime() - otherEvent.getEventTime(); 
    }

    Customer getCustomer() {
        return customer;
    }

    double getEventTime() {
        return eventTime;
    }

    void execute(PriorityQueue<RandomEvent> pq, 
        Server[] servers,
        Stats stats,
        RandomGenerator rng,
        double restingProbability) {

        switch (eventType) {

            case "Arrive":

                if (!customer.isGreedy()) {

                    boolean leave = true;

                    // check if can serve
                    for (int i = 0; i < servers.length; i++) {
                        if (servers[i].canServe() && !servers[i].hasQueue()) {
                            servers[i] = servers[i].queueCustomer(this.getCustomer());
                            leave = false;
                            pq.add(new RandomEvent("Serve", 
                                getCustomer(), 
                                servers[i], 
                                this.getEventTime()));
                            break;
                        }
                    }
                    
                    // check if can wait
                    if (leave == true) {
                        for (int i = 0; i < servers.length; i++) {
                            if (!servers[i].queueIsFull()) {
                                servers[i] = servers[i].queueCustomer(this.getCustomer());
                                leave = false;
                                pq.add(new RandomEvent("Wait", 
                                    getCustomer(), 
                                    servers[i], 
                                    this.getEventTime()));
                                break;
                            }
                        }
                    }
                    

                    // otherwise leave
                    if (leave == true) {
                        pq.add(new RandomEvent("Leave", getCustomer(), this.getEventTime()));
                    }

                    System.out.println(this);

                } else if (customer.isGreedy()) {

                    boolean leave = true;

                    // check if can serve
                    for (int i = 0; i < servers.length; i++) {
                        if (servers[i].canServe() && !servers[i].hasQueue()) {
                            servers[i] = servers[i].queueCustomer(this.getCustomer());
                            leave = false;
                            pq.add(new RandomEvent("Serve", 
                                getCustomer(), 
                                servers[i], 
                                this.getEventTime()));
                            break;
                        }
                    }

                    // check if can wait
                    int shortestQueue = -1;
                    int shortestQueueLength = Integer.MAX_VALUE;
                    if (leave == true) {
                        for (int i = 0; i < servers.length; i++) {
                            if (!servers[i].queueIsFull()) {
                                if (servers[i].queueLength() < shortestQueueLength) {
                                    shortestQueue = i;
                                    shortestQueueLength = servers[i].queueLength();
                                }
                                
                            }
                        }
                        if (shortestQueue > -1) {
                            servers[shortestQueue] = servers[shortestQueue]
                                .queueCustomer(this.getCustomer());
                            leave = false;
                            pq.add(new RandomEvent("Wait", 
                                getCustomer(), 
                                servers[shortestQueue], 
                                this.getEventTime()));
                        }
                        
                    }

                    // otherwise leave
                    if (leave == true) {
                        pq.add(new RandomEvent("Leave", getCustomer(), this.getEventTime()));
                    }

                    System.out.println(this);

                }

                break;

            case "Wait":

                System.out.println(this);

                break;

            case "Serve":

                double timeToDone = getCustomer().getServingTime();
                pq.add(new RandomEvent("Done",
                    getCustomer(), 
                    server, 
                    this.getEventTime() + timeToDone));

                double waitingTime = this.getEventTime() - this.getCustomer().getArrivalTime();

                stats.addWaitingTime(waitingTime);
                
                int serverNum = server.getIdentifier();
                servers[serverNum - 1] = servers[serverNum - 1].serve();

                System.out.println(this);

                break;

            case "Done":

                serverNum = server.getIdentifier();
                servers[serverNum - 1] = servers[serverNum - 1].doneServing();

                if (server.needsRest()) {
                    stats.addHumanServed();
                } else {
                    stats.addScoServed();
                }

                

                double restTime = 0;
                if (server.needsRest()) {
                    double restingChance = rng.genRandomRest();
                    boolean needsToRest = restingChance < restingProbability;
                    if (needsToRest) {
                        restTime = rng.genRestPeriod();
                    }
                }

                if (restTime != 0) {
                    pq.add(new RandomEvent("Rest", 
                        this.getCustomer(), 
                        server, 
                        this.getEventTime(), 
                        restTime));

                } else if (server.hasQueue()) {
                    pq.add(new RandomEvent("Serve", 
                        server.nextCustomer(), 
                        server, 
                        this.getEventTime()));
                }

                System.out.println(this);

                break;

            case "Rest":

                serverNum = server.getIdentifier();
                servers[serverNum - 1] = server.takeRest();

                pq.add(new RandomEvent("RestEnd", 
                    this.getCustomer(), 
                    server, 
                    this.getEventTime() + this.restTime));

                break;

            case "RestEnd":

                serverNum = server.getIdentifier();
                servers[serverNum - 1] = server.stopResting();
                    
                if (servers[serverNum - 1].hasQueue()) {
                    pq.add(new RandomEvent("Serve", 
                        server.nextCustomer(), 
                        server, 
                        this.getEventTime()));
                }

                break;

            case "Leave":

                stats.addTotalLeave();

                System.out.println(this);

                break;

            default:

                break;

        }
    }

    @Override
    public String toString() {

        String toPrint = "";
        String greedy = "";
        if (customer.isGreedy()) {
            greedy = "(greedy)";
        }

        switch (eventType) {

            case "Arrive":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() + greedy + " arrives";

                break;

            case "Wait":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() 
                    + greedy 
                    + " waits at " 
                    + server.toString(); 

                break;

            case "Serve":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() 
                    + greedy 
                    + " serves by " 
                    + server.toString();

                break;

            case "Done":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() 
                    + greedy 
                    + " done serving by " 
                    + server.toString(); 

                break;   

            case "Leave":

                toPrint += String.format("%.3f", getEventTime()) + " " 
                    + getCustomer().getIdentifier()
                    + greedy 
                    + " leaves";

                break;

            default:

                break;

        }

        return toPrint;
    }

}
