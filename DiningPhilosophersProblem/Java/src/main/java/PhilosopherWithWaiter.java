public class PhilosopherWithWaiter implements Runnable{
    int id;
    int N;
    int iter_num = 25;
    Fork[] forks;
    Waiter waiter;

    public long time = 0;

    public PhilosopherWithWaiter(int id, int N, Waiter waiter, Fork[] forks) {
        this.id = id;
        this.N = N;
        this.waiter = waiter;
        this.forks = forks;
    }

    private void think() throws InterruptedException {
        Thread.sleep(1);
    }

    private void eat() throws InterruptedException {
        Thread.sleep(1);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < iter_num; i++) {
                think();
                try {
                    long startTime = System.nanoTime();
                    waiter.startEating();
                    forks[id].take();
                    forks[(id + 1) % N].take();
                    //System.out.println("Start eating - philosopher no:" + id);
                    long endTime = System.nanoTime();
                    this.time += (endTime - startTime);
                }
                catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                eat();
                forks[id].put();
                forks[(id + 1) % N].put();
                //System.out.println("End eating - philosopher no: " + id);
                waiter.startThinking();
            }
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
