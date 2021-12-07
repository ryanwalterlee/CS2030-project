package cs2030.simulator;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Optional;

class HumanServer implements Server {
    private final int identifier;
    private final Customer customer;
    private final Queue<Customer> customerQueue;
    private final int maxLength;
    private final boolean isResting;

    // basic constructor
    HumanServer(int identifier, int maxLength) {
        this.customer = new Customer(0, 0, 0);
        this.customerQueue = new LinkedList<>();
        this.identifier = identifier;
        this.maxLength = maxLength;
        this.isResting = false;
    }

    // constructor for serving and queueing new customer
    HumanServer(Customer customer, 
        Queue<Customer> customerQueue, 
        int identifier, 
        int maxLength) {

        this.customer = customer;
        this.identifier = identifier;
        this.customerQueue = customerQueue;
        this.maxLength = maxLength;
        this.isResting = false;
    }

    // constructor for done serving customer and resting
    HumanServer(Queue<Customer> customerQueue, 
        int identifier, 
        int maxLength, 
        boolean rest) {

        this.customer = new Customer(0, 0, 0);
        this.customerQueue = customerQueue;
        this.identifier = identifier;
        this.maxLength = maxLength;
        this.isResting = rest;
    }


    // serves the next customer in queue at this timing
    public Server serve() {
        Customer customerToServe = customerQueue.poll();
        return new HumanServer(customerToServe, customerQueue, 
            identifier, maxLength);
    }

    // adds customer into the queue
    public Server queueCustomer(Customer newCustomer) {
        customerQueue.add(newCustomer);
        return new HumanServer(customer, customerQueue, 
                identifier, maxLength);
    }

    public Server doneServing() {
        return new HumanServer(customerQueue, identifier, maxLength, false);
    }

    public Server takeRest() {
        return new HumanServer(customerQueue, identifier, maxLength, true);
    }

    public Server stopResting() {
        return new HumanServer(customerQueue, identifier, maxLength, false);
    }

    public Customer nextCustomer() {
        return customerQueue.peek();
    }

    public int getIdentifier() {
        return identifier;
    }

    public Customer getCustomer() {
        return customer;
    }

    // check if queue is full
    public boolean queueIsFull() {
        return customerQueue.size() == maxLength;
    }

    // check if there is any queue
    public boolean hasQueue() {
        if (customerQueue.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int queueLength() {
        return customerQueue.size();
    }

    // check if server is free to serve
    public boolean canServe() {
        if (this.customer.getIdentifier() == 0 && this.isResting == false) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isResting() {
        return isResting;
    }

    public boolean needsRest() {
        return true;
    }

    @Override
    public String toString() {
        return "server " + identifier;
    }
}
