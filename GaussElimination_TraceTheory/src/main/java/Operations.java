public class Operations implements Runnable {
    private final char operation;
    private final int row;
    private final double[][] m;
    private final double[][] M;
    private final double[][][] n;
    private final int size;

    public Operations(double[][] m, double[][][] n,double[][] M, char operation, int row, int size) {
        this.operation = operation;
        this.row = row;
        this.m = m;
        this.M = M;
        this.n = n;
        this.size = size;
    }

    public void A(int i, int k) {
        i-=1;
        k-=1;
        m[k][i] = M[k][i] / M[i][i];
    }

    public void B(int i, int j, int k) {
        i-=1;
        j-=1;
        k-=1;
        n[k][j][i] = M[i][j] * m[k][i];
    }

    public void C(int i, int j, int k) {
        i-=1;
        j-=1;
        k-=1;
        M[k][j] -= n[k][j][i];
    }

    @Override
    public void run() {
        int i = row;
        int j, k;
        int num = Integer.parseInt(Thread.currentThread().getName().trim().split("-")[3]);

        j = i + (num - 1) % (size + 2 - i);

        int k2 = i + 1 + (num - 1) / (size + 2 - i);
        switch (operation) {
            case 'A':
                k = i + num;
                A(i, k);
                break;
            case 'B':
                k = k2;
                B(i, j, k);
                break;
            case 'C':
                k = k2;
                C(i, j, k);
                break;
            default:
                System.out.println("Wrong operation");
                System.exit(-1);
        }
    }
}
