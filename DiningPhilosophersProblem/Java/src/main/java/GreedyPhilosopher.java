import java.util.concurrent.locks.ReentrantLock;

public class GreedyPhilosopher implements Runnable {
    private final int id;
    static int iter_num = 25;
    long time = 0;

    private final Object[] forks;
    private final ReentrantLock[] locks;

    Object left_fork = null;
    Object right_fork = null;

    public GreedyPhilosopher(int id, ReentrantLock[] locks, Object[] forks){
        this.id  = id;
        this.locks = locks;
        this.forks = forks;
    }

    private void think() throws InterruptedException{
        Thread.sleep(1);
    }

    private void eat() throws InterruptedException{
        Thread.sleep(1);
    }

    public void run(){
        try{
            int i = 0;
            while( i < iter_num){
                think();
                int left = id;
                int right = (id + 1)% locks.length;
                long startTime = System.nanoTime();
                if(locks[left].tryLock()){
                    try{
                        if(locks[right].tryLock()){
                            try{
                                long endTime = System.nanoTime();
                                this.time += (endTime - startTime);
                                this.left_fork = forks[left];
                                this.right_fork = forks[right];
                                //System.out.println("Start eating - philosopher no:" + id);
                                eat();
                                i += 1;
                            }
                            finally {
                                this.right_fork = null;
                                locks[right].unlock();
                            }
                        }
                    }
                    finally {
                        this.left_fork = null;
                        locks[left].unlock();
                        //System.out.println("End eating - philosopher no: " + id);
                    }
                }

            }
        }
        catch(InterruptedException exception){
            exception.printStackTrace();
        }

    }

}
