import cs2030.simulator.Simulator;
import java.util.ArrayList;
import java.util.Scanner;

class Main3 {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        int numServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();

        ArrayList<Double> listOfArrivalTimes = new ArrayList<>();
        ArrayList<Double> listOfServingTimes = new ArrayList<>();
        ArrayList<Double> listOfRestTimes = new ArrayList<>();

        for (int i = 0; i < numOfCustomers; i++) {
            listOfArrivalTimes.add(sc.nextDouble());
            listOfServingTimes.add(sc.nextDouble());
        }

        for (int i = 0; i < numOfCustomers; i++) {
            listOfRestTimes.add(sc.nextDouble());
        }

        Simulator s = new Simulator();

        s.simulate(listOfArrivalTimes, 
            listOfServingTimes,
            listOfRestTimes, 
            maxQueueLength, 
            numServers,
            0);
        sc.close();
    }
}
