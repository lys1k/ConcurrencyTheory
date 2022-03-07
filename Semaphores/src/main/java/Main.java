public class Main {
    private static int iter_num = 10000;
    private static void test(Counter counter) {
        var inc = new Thread(() -> {
            for(int i = 0; i < iter_num; i++){
                counter.increment();
            }
        });

        var dec = new Thread(() ->{
            for(int i = 0; i < iter_num; i++){
                counter.decrement();
            }
        });

        inc.start();
        dec.start();

        try{
            dec.join();
            inc.join();
            System.out.println("Counter value equals " + counter.getValue() + "\n");
        }
        catch (InterruptedException exception){
            exception.printStackTrace();
        }

    }


    public static void main(String[] args) throws InterruptedException{
        System.out.println("Task 1: ");
        test(new Counter(new BinarySemaphore()));

        System.out.println("Task 2: ");
        System.out.println("Nalezy odkomentowac test. \n");
        //test(new Counter((new BinarySemaphoreIf())));
        //test zakomentowany poniewaz moze dochodzic do zakleszczen.

        /*
        Semafor binarny nie będzie dzialał prawidłowo gdy zamiast while użyjemy if,
        ponieważ w tym samym czasie dwa wątki mogą czekaą na siebie nawzajem, czyli powstanie zakleszczenie watkow.
        Istnieje mozliwosc znalezienia się dwóch wątków w strefie krytcznej (np. z powodu wywlaszczenia)
        co może prowadzić do nieprawidłowych wyników.
         */

        System.out.println("Task 3: ");
        test(new Counter(new CountingSemaphore(1)));

        /*
        Semafor binarny jest szczególnym przypadkiem semafora ogólnego,
        ponieważ możemy utworzyć semafor ogólny z wartością 1, który będzie
        działać jak semafor binarny.
        W semaforze binarnym użylibyśmy: true/ false, natomiast w semaforze
        ogólnym wartości 0/1.

         */

    }
}