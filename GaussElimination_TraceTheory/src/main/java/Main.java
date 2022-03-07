import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static char operation;
    private static int size;
    static double[][] M;
    static double[][] m;
    static double[][][] n;

    public static void loadMatrix(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Scanner scanner = new Scanner(bufferedReader);
            if(scanner.hasNextLine()) {
                size = Integer.parseInt(scanner.nextLine().trim().split(" ")[0]);
                M = new double[size][size+1];
                try {
                    for (int i = 0; i < size + 1; i++) {
                        if (scanner.hasNextLine()) {
                            String[] line = scanner.nextLine().trim().split(" ");
                            for (int j = 0; j < size; j++) {
                                if(i < size) {
                                    M[i][j] = Double.parseDouble(line[j]);
                                }
                                else{
                                    M[j][size] = Double.parseDouble(line[j]);
                                }
                            }
                        }
                    }
                    scanner.close();

                } catch(IndexOutOfBoundsException e) {
                    System.out.println("Index out of bounds exceptions");
                    System.exit(-1);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void saveMatrix(String filename){
        PrintStream stdout = System.out;
        try {
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);

            System.out.println(size);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    System.out.print(M[i][j] + " ");
                }
                System.out.println();
            }
            for (int i = 0; i < size; i++) {
                System.out.print(M[i][size] + " ");
            }
            fos.close();
            ps.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.setOut(stdout);
    }

    public static void runThreads(int rowsNumber, int row) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(rowsNumber);
        List<Future<?>> runningThreads = new LinkedList<>();

        for(int i = 0; i < rowsNumber; i++) {
            runningThreads.add(executor.submit(new Operations(m, n,M, operation, row,  size)));
        }
        for(Future<?> f : runningThreads) {
            f.get();
        }
        executor.shutdown();
    }

    public static void scheduler() throws ExecutionException, InterruptedException {
        for(int row = 1; row < size; row++) {
            int rowsNumber = size - row;
            operation = 'A';
            runThreads(rowsNumber,row);

            int A_B_operations = rowsNumber * (size + 2 - row);
            operation = 'B';
            runThreads(A_B_operations, row);

            operation = 'C';
            runThreads(A_B_operations,row);
        }
    }

    public static void GaussElimination(){
        for (int i = size - 1; i >= 0; i--) {
            for (int k = i - 1; k >= 0; k--) {
                double multiplier = M[k][i] / M[i][i];
                M[k][i] -= multiplier * M[i][i];
                M[k][size] -= multiplier * M[i][size];
            }
            M[i][size] /= M[i][i];
            M[i][i] /= M[i][i];
        }

    }

    public static void main(final String[] args) {
        if(args.length!= 2){
            System.out.println("Wrong number of arguments");
            System.exit(-1);
        }
        loadMatrix(args[0]);
        m = new double[size][size];
        n = new double[size][size+1][size];

        try {
            scheduler();
        } catch(InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        GaussElimination();
        saveMatrix(args[1]);

        System.out.println("Result saved to file: " + args[1]);
    }
}