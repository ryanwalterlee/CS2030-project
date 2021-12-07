import cs2030.simulator.Simulator;
import java.util.ArrayList;
import java.util.Scanner;

class Main5 {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        int seed = sc.nextInt();
        int numServers = sc.nextInt();
        int numSelfCheckout = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numCustomers = sc.nextInt();
        double arrivalRate = sc.nextDouble();
        double serviceRate = sc.nextDouble();
        double restingRate = sc.nextDouble();
        double restingProbability = sc.nextDouble();
        double greedyProbability = sc.nextDouble();
   

        Simulator s = new Simulator();

        s.simulate(seed,
            numServers,
            numSelfCheckout,
            maxQueueLength,
            numCustomers,
            arrivalRate,
            serviceRate,
            restingRate,
            restingProbability,
            greedyProbability);
        
        sc.close();
    }
}
