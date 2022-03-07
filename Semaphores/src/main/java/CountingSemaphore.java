public class CountingSemaphore implements ISemaphore {
    private int value;
    private final BinarySemaphore gate;
    private final BinarySemaphore mutex;

    public CountingSemaphore(int value){
        this.value = value;
        this.gate = new BinarySemaphore();
        this.mutex = new BinarySemaphore();
    }

    @Override
    public void P(){
        gate.P();
        mutex.P();
        value --;
        if(value > 0){
            gate.V();
        }
        mutex.V();
    }

    @Override
    public void V(){
        mutex.P();
        value ++;
        if(value == 1){
            gate.V();
        }
        mutex.V();
    }
}