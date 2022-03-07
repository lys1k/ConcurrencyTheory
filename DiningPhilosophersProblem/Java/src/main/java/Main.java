import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static int iter_num = 25;

    private static void greedy(int N) throws InterruptedException{
        ReentrantLock[] locks = new ReentrantLock[N];
        Object[] forks = new Object[N];

        for(int j = 0; j < N ; j++){
            locks[j] = new ReentrantLock();
            forks[j] = new Object();
        }
        GreedyPhilosopher[] philosophers = new GreedyPhilosopher[N];
        for(int k = 0 ; k < N; k++){
            philosophers[k] = new GreedyPhilosopher(k, locks, forks);
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for(int j = 0 ; j < N; j++){
            Thread thread = new Thread(philosophers[j]);
            thread.start();
            threads.add(thread);
        }
        for(Thread thread: threads){
            thread.join();
        }
        for(int i = 0; i < philosophers.length; i+=1 ){
            System.out.println("Philosopher no: " +i + " average time: " + philosophers[i].time/iter_num + " [ns]" );
        }
        System.out.println("\n");

    }

    public static void waiter(int N) throws InterruptedException{
        Waiter waiter = new Waiter(N - 1);
        Fork[] forks = new Fork[N];

        for (int i = 0; i < N; i++)
            forks[i] = new Fork();

        PhilosopherWithWaiter[] philosophers = new PhilosopherWithWaiter[N];
        for (int i = 0; i < N; i++)
            philosophers[i] = new PhilosopherWithWaiter(i, N,  waiter, forks);

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Thread newProducer = new Thread(philosophers[i]);
            newProducer.start();
            threads.add(newProducer);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for(int i = 0 ; i < philosophers.length; i++){
           System.out.println("Philosopher no: " + i + " average time: " + philosophers[i].time/iter_num + "[ns]" );

        }
        System.out.println("\n");

    }

    public static void main(String[] args) throws InterruptedException{
        int[] philosopher_num = {5, 10, 15};


        for(int num :  philosopher_num){
            System.out.println("Greedy Philosopher: ");
            System.out.println("Number of philosophers: " + num);
            greedy(num);
        }

        for(int num :  philosopher_num){
            System.out.println("Philosopher with waiter ");
            System.out.println("Number of philosophers: " + num);
            waiter(num);
        }

    }
}
