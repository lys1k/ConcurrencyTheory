public class BinarySemaphore implements ISemaphore {
    private boolean wait = false;

    @Override
    public synchronized void P(){
        while(wait){
            try{
                wait();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        wait = true;
        notifyAll();
    }

    @Override
    public synchronized void V() {
        while(!wait) {
            try {
                this.wait();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
        wait = false;
        notifyAll();
    }
}