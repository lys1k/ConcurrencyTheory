public class Waiter {
    int semaphore = 0;
    int philosopher_max_num;

    public Waiter(int philosopher_max_num){
       this.philosopher_max_num = philosopher_max_num;
   }

    public synchronized void startEating() throws InterruptedException{
        while(semaphore == philosopher_max_num){
            wait();
        }
        semaphore+=1;
    }

    public synchronized void startThinking() throws  InterruptedException{
        if(semaphore > 0){
           notify();
        }
        semaphore-=1;
    }

}
