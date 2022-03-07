public class BinarySemaphoreIf implements ISemaphore{
    private boolean wait;

    @Override
    public synchronized void P(){
        if(wait){
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
        if(!wait) {
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