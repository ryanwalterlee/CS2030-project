package cs2030.simulator;

import java.util.Comparator;

class RandomEventComparator implements Comparator<RandomEvent> {
    public int compare(RandomEvent event1, RandomEvent event2) {
        if (event1.compare(event2) > 0.0) {
            return 1;
        } else if (event1.compare(event2) < 0.0) {
            return -1;
        } else {
            return event1.getCustomer().getIdentifier() - event2.getCustomer().getIdentifier();
        }
        
    }
}
