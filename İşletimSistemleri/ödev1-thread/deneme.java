import java.util.Random;

public class deneme {
    public static void main(String[] args) {
        int N = 10; // Array/Matris boyutu
        int M = 2;  // Alt kümelerin sayısı

        int[] array = generateRandomArray(N);
        int[][] matrix = generateRandomMatrix(N);

        // Array işlemleri için iş parçacıkları oluştur
        for (int i = 0; i < M; i++) {
            int start = i * (N / M);
            int end = (i == M - 1) ? N : (i + 1) * (N / M);

            Thread arrayThread = new Thread(new ArrayOperations(array, start, end, "Array Operation " + (i+1)));
            arrayThread.start();
        }

        // Matris işlemleri için iş parçacıkları oluştur
        for (int i = 0; i < N; i++) {
            Thread matrixThread = new Thread(new MatrixOperations(matrix[i], "Matrix Row " + (i+1)));
            matrixThread.start();
        }
    }

    // Verilen boyutta rastgele bir array oluşturur
    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(10) + 1; // 1 ile 10 arası rastgele sayılar
        }
        return array;
    }

    // Verilen boyutta rastgele bir matris oluşturur
    public static int[][] generateRandomMatrix(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(10) + 1; // 1 ile 10 arası rastgele sayılar
            }
        }
        return matrix;
    }

    // Array işlemlerini yapan iş parçacığı
    static class ArrayOperations implements Runnable {
        private int[] array;
        private int start;
        private int end;
        private String name;

        public ArrayOperations(int[] array, int start, int end, String name) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.name = name;
        }

        @Override
        public void run() {
            long startTime = System.nanoTime();

            int max = array[start];
            int min = array[start];
            int sum = 0;
            for (int i = start; i < end; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
                if (array[i] < min) {
                    min = array[i];
                }
                sum += array[i];
            }
            double average = (double) sum / (end - start);

            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            System.out.println(name + " - Thread ID: " + Thread.currentThread().getId());
            System.out.println("Max: " + max);
            System.out.println("Min: " + min);
            System.out.println("Sum: " + sum);
            System.out.println("Average: " + average);
            System.out.println("Duration: " + duration + " nanoseconds");
            System.out.println();
        }
    }

    // Matris işlemlerini yapan iş parçacığı
    static class MatrixOperations implements Runnable {
        private int[] row;
        private String name;

        public MatrixOperations(int[] row, String name) {
            this.row = row;
            this.name = name;
        }

        @Override
        public void run() {
            long startTime = System.nanoTime();

            int max = row[0];
            int min = row[0];
            int sum = 0;
            for (int num : row) {
                if (num > max) {
                    max = num;
                }
                if (num < min) {
                    min = num;
                }
                sum += num;
            }
            double average = (double) sum / row.length;

            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            System.out.println(name + " - Thread ID: " + Thread.currentThread().getId());
            System.out.println("Max: " + max);
            System.out.println("Min: " + min);
            System.out.println("Sum: " + sum);
            System.out.println("Average: " + average);
            System.out.println("Duration: " + duration + " nanoseconds");
            System.out.println();
        }
    }
}
