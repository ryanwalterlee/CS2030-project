package cs2030.simulator;

import java.util.Queue;
import java.util.LinkedList;

interface Server {

    // serves the next customer in queue at this timing
    Server serve();

    // adds customer into the queue
    Server queueCustomer(Customer newCustomer);

    Server doneServing();

    Server takeRest();

    boolean needsRest();

    Server stopResting();

    Customer nextCustomer();

    int getIdentifier();

    Customer getCustomer();

    // check if queue is full
    boolean queueIsFull();

    // check if there is any queue
    boolean hasQueue();

    // check if server is free to serve
    boolean canServe();

    // check if server is resting
    boolean isResting();

    int queueLength();

}
