import cs2030.simulator.Simulator;
import java.util.ArrayList;
import java.util.Scanner;

class Main2 {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        int numServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();

        ArrayList<Double> listOfArrivalTimes = new ArrayList<>();
        ArrayList<Double> listOfServingTimes = new ArrayList<>();
        ArrayList<Double> listOfRestTimes = new ArrayList<>();

        while (sc.hasNextDouble()) {
            listOfArrivalTimes.add(sc.nextDouble());
            listOfServingTimes.add(sc.nextDouble());
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
