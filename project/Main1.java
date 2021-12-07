import cs2030.simulator.Simulator;
import java.util.ArrayList;
import java.util.Scanner;

class Main1 {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        int numServers = sc.nextInt();

        ArrayList<Double> listOfArrivalTimes = new ArrayList<>();
        ArrayList<Double> listOfServingTimes = new ArrayList<>();
        ArrayList<Double> listOfRestTimes = new ArrayList<>();

        int maxQueueLength = 1;

        while (sc.hasNextDouble()) {
            listOfArrivalTimes.add(sc.nextDouble());
            listOfServingTimes.add(1.0);
            listOfRestTimes.add(0.0);
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
