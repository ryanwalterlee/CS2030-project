package cs2030.simulator;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Optional;

class SelfCheckout implements Server {
    private final int identifier;
    private final Customer customer;
    private static final Queue<Customer> customerQueue = new LinkedList<>();
    private final int maxLength;

    // basic constructor
    SelfCheckout(int identifier, int maxLength) {
        this.customer = new Customer(0, 0, 0);
        this.identifier = identifier;
        this.maxLength = maxLength;
    }

    // constructor for serving and queueing new customer
    SelfCheckout(Customer customer, int identifier, int maxLength) {
        this.customer = customer;
        this.identifier = identifier;
        this.maxLength = maxLength;
    }

    // serves the next customer in queue at this timing
    public Server serve() {
        Customer customerToServe = customerQueue.poll();
        return new SelfCheckout(customerToServe, 
            identifier, maxLength);
    }

    // adds customer into the queue
    public Server queueCustomer(Customer newCustomer) {
        customerQueue.add(newCustomer);
        return new SelfCheckout(customer, identifier, maxLength);       
    }

    public Server doneServing() {
        return new SelfCheckout(identifier, maxLength);
    }

    public Server takeRest() {
        return this;
    }

    public Server stopResting() {
        return this;
    }

    public boolean needsRest() {
        return false;
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
        if (this.customer.getIdentifier() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isResting() {
        return false;
    }

    @Override
    public String toString() {
        return "self-check " + identifier;
    }
}
