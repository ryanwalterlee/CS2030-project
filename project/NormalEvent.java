package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Optional;

class NormalEvent {
    
    private final Customer customer;
    private final Server server;
    private final double eventTime;
    private final String eventType;

    NormalEvent(String eventType, Customer customer, double time) {
        this.customer = customer;
        this.eventTime = time;
        this.eventType = eventType;
        this.server = new HumanServer(0, 0);
    }

    NormalEvent(String eventType, Customer customer, Server server, double time) {
        this.customer = customer;
        this.eventTime = time;
        this.eventType = eventType;
        this.server = server;
    }

    double compare(NormalEvent otherEvent) {
        return this.getEventTime() - otherEvent.getEventTime(); 
    }

    Customer getCustomer() {
        return customer;
    }

    double getEventTime() {
        return eventTime;
    }

    void execute(PriorityQueue<NormalEvent> pq, 
        Server[] servers, 
        ArrayList<Double> listOfRestTimes,
        Stats stats) {

        switch (eventType) {

            case "Arrive":

                boolean leave = true;

                // check if can serve
                for (int i = 0; i < servers.length; i++) {
                    if (servers[i].canServe() && !servers[i].hasQueue()) {
                        servers[i] = servers[i].queueCustomer(this.getCustomer());
                        leave = false;
                        pq.add(new NormalEvent("Serve", 
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
                            pq.add(new NormalEvent("Wait", 
                                getCustomer(), 
                                servers[i], 
                                this.getEventTime()));
                            break;
                        }
                    }
                }
                

                // otherwise leave
                if (leave == true) {
                    pq.add(new NormalEvent("Leave", getCustomer(), this.getEventTime()));
                }

                System.out.println(this);

                break;


            case "Wait":

                System.out.println(this);

                break;

            case "Serve":

                double timeToDone = getCustomer().getServingTime();
                pq.add(new NormalEvent("Done", 
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
                    restTime = listOfRestTimes.get(stats.getHumanServed() - 1);
                }

                if (restTime != 0) {
                    pq.add(new NormalEvent("Rest", 
                        this.getCustomer(), 
                        server, 
                        this.getEventTime()));
                } else if (server.hasQueue()) {
                    pq.add(new NormalEvent("Serve", 
                        server.nextCustomer(), 
                        server, 
                        this.getEventTime()));
                }

                System.out.println(this);

                break;

            case "Rest":

                serverNum = server.getIdentifier();
                servers[serverNum - 1] = server.takeRest();
                
                double rest = listOfRestTimes.get(stats.getHumanServed() - 1);

                pq.add(new NormalEvent("RestEnd", 
                    this.getCustomer(), 
                    server, 
                    this.getEventTime() + rest));

                break;

            case "RestEnd":

                serverNum = server.getIdentifier();
                servers[serverNum - 1] = server.stopResting();
                    
                if (servers[serverNum - 1].hasQueue()) {
                    pq.add(new NormalEvent("Serve", 
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

        switch (eventType) {

            case "Arrive":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() 
                    + " arrives";

                break;

            case "Wait":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() 
                    + " waits at " 
                    + server.toString(); 

                break;

            case "Serve":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() 
                    + " serves by " 
                    + server.toString();

                break;

            case "Done":

                toPrint += String.format("%.3f", this.getEventTime()) + " " 
                    + getCustomer().getIdentifier() 
                    + " done serving by " 
                    + server.toString(); 

                break;   

            case "Leave":

                toPrint += String.format("%.3f", getEventTime()) + " " 
                    + getCustomer().getIdentifier() + " leaves";

                break;

            default:

                break;

        }

        return toPrint;
    }

}
