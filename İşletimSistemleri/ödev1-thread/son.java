import java.util.Arrays;
import java.util.Random;

public class son {

    public static void main(String[] args) {
        int minN = 10;
        int maxN = 100000;
        int minM = 10;
        int maxM = 1000;

        Random random = new Random();
        int numIterations = 2;

        for (int N = minN; N <= maxN; N *= 5) {
            for (int M = minM; M <= maxM; M *= 5) {
                long totalThreadArrayDuration = 0;
                long totalThreadMatrixDuration = 0;
                long totalArrayDuration = 0;
                long totalMatrixDuration = 0;

                for (int i = 0; i < numIterations; i++) {
                    int[] array = new int[N];
                    for (int j = 0; j < N; j++) {
                        array[j] = random.nextInt(1000) + 1;
                    }
                    long threadArrayDuration = measureThreadArrayOperations(array);

                    int[][] matrix = new int[N][N];
                    for (int j = 0; j < N; j++) {
                        for (int k = 0; k < N; k++) {
                            matrix[j][k] = random.nextInt(1000) + 1;
                        }
                    }
                    long threadMatrixDuration = measureThreadMatrixOperations(matrix);
                    long arrayDuration = measureArrayOperations(array);
                    long matrixDuration = measureMatrixOperations(matrix);

                    totalThreadArrayDuration += threadArrayDuration;
                    totalThreadMatrixDuration += threadMatrixDuration;
                    totalArrayDuration += arrayDuration;
                    totalMatrixDuration += matrixDuration;
                }

                long avgThreadArrayDuration = totalThreadArrayDuration / numIterations;
                long avgThreadMatrixDuration = totalThreadMatrixDuration / numIterations;
                long avgArrayDuration = totalArrayDuration / numIterations;
                long avgMatrixDuration = totalMatrixDuration / numIterations;

                System.out.println("N = " + N + ", M = " + M);
                System.out.println("Thread kullanarak Array (mikrosaniye): " + avgThreadArrayDuration);
                System.out.println("Thread kullanarak Matrix (mikrosaniye): " + avgThreadMatrixDuration);
                System.out.println("Array kullanarak(thread yok) (mikrosaniye): " + avgArrayDuration);
                System.out.println("Matrix kullanarak(thread yok) (mikrosaniye): " + avgMatrixDuration);
                System.out.println();
            }
        }
    }

    public static long measureThreadArrayOperations(int[] array) {
        long startTime = System.nanoTime();

        ThreadArrayOperations threadArrayOperations = new ThreadArrayOperations(array);
        threadArrayOperations.start();

        try {
            threadArrayOperations.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    public static long measureThreadMatrixOperations(int[][] matrix) {
        long startTime = System.nanoTime();

        ThreadMatrixOperations threadMatrixOperations = new ThreadMatrixOperations(matrix);
        threadMatrixOperations.start();

        try {
            threadMatrixOperations.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    public static long measureArrayOperations(int[] array) {
        long startTime = System.nanoTime();
        Arrays.sort(array);
        long sum = 0;
        for (int num : array) {
            sum += num;
        }
        double average = (double) sum / array.length;
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    public static long measureMatrixOperations(int[][] matrix) {
        long startTime = System.nanoTime();

        transposeMatrix(matrix);
        long sum = 0;
        for (int[] row : matrix) {
            for (int num : row) {
                sum += num;
            }
        }
        System.out.println("Matris Toplamı: " + sum);

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000;
    }

    static class ThreadArrayOperations extends Thread {
        private int[] array;

        public ThreadArrayOperations(int[] array) {
            this.array = array;
        }

        public void run() {
            long sum = 0;
            for (int num : array) {
                sum += num;
            }
            double average = (double) sum / array.length;

            System.out.println("Array Toplamı: " + sum);
            System.out.println("Array Ortalaması: " + average);
        }
    }

    static class ThreadMatrixOperations extends Thread {
        private int[][] matrix;

        public ThreadMatrixOperations(int[][] matrix) {
            this.matrix = matrix;
        }

        public void run() {
            transposeMatrix(matrix);
            long sum = 0;
            for (int[] row : matrix) {
                for (int num : row) {
                    sum += num;
                }
            }
            System.out.println("Matris Toplamı: " + sum);
        }

        private void transposeMatrix(int[][] matrix) {
            int size = matrix.length;
            int[][] result = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    result[i][j] = matrix[j][i];
                }
            }
        }
    }
}
