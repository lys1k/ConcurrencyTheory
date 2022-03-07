public class Counter {
    private int value = 0;
    private final ISemaphore semaphore;

    public Counter(ISemaphore semaphore){
        this.value = 0;
        this.semaphore = semaphore;

    }

    public void increment(){
        semaphore.P();
        value++ ;
        semaphore.V();
    }

    public void decrement(){
        semaphore.P();
        value--;
        semaphore.V();
    }

    public int getValue(){
        return value;
    }
}