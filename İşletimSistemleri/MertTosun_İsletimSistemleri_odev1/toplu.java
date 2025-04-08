import java.util.Arrays;
import java.util.Random;

public class toplu {

    public static void main(String[] args) {
        
        int minN = 10;
        int maxN = 100000;
        int minM = 10;
        int maxM = 1000;

        // Rastgele N ve M değerleri üretme
        Random random = new Random();
        int numIterations = 5; // Her kombinasyon için 5 kez çalıştıracağız. 

        for (int N = minN; N <= maxN; N *= 10) {
            for (int M = minM; M <= maxM; M *= 10) {
                long totalThreadArrayDuration = 0;
                long totalThreadMatrixDuration = 0;
                long totalArrayDuration = 0;
                long totalMatrixDuration = 0;

                for (int i = 0; i < numIterations; i++) {
                    // N elemanlı array oluşturma 
                    int[] array = new int[N];
                    for (int j = 0; j < N; j++) {
                        array[j] = random.nextInt(1000) + 1; // 1-1000 arası rastgele sayılar atıyoruz
                    }
                    long threadArrayDuration = measureThreadArrayOperations(array);

                    // NxN boyutunda matrix oluşturma
                    int[][] matrix = new int[N][N];
                    for (int j = 0; j < N; j++) {
                        for (int k = 0; k < N; k++) {
                            matrix[j][k] = random.nextInt(1000) + 1; // 1-1000 arası rastgele sayılar
                        }
                    }
                    long threadMatrixDuration = measureThreadMatrixOperations(matrix);

                    // N elemanlı array işlemler
                    long arrayDuration = measureArrayOperations(array);

                    // NxN boyutunda matrix işlemler
                    long matrixDuration = measureMatrixOperations(matrix);

                    // Toplam süreleri hesapla
                    totalThreadArrayDuration += threadArrayDuration;
                    totalThreadMatrixDuration += threadMatrixDuration;
                    totalArrayDuration += arrayDuration;
                    totalMatrixDuration += matrixDuration;
                }

                // Ortalama süreleri hesapla
                long avgThreadArrayDuration = totalThreadArrayDuration / numIterations;
                long avgThreadMatrixDuration = totalThreadMatrixDuration / numIterations;
                long avgArrayDuration = totalArrayDuration / numIterations;
                long avgMatrixDuration = totalMatrixDuration / numIterations;

                // Sonuçları yazdır
                System.out.println("Değiken(N) = " + N + ",ALT KÜME (M) = " + M);
                System.out.println("Thread Array Süresi (mikrosaniye): " + avgThreadArrayDuration);
                System.out.println("Thread Matrix Süresi (mikrosaniye): " + avgThreadMatrixDuration);
                System.out.println("Array Süresi (mikrosaniye): " + avgArrayDuration);
                System.out.println("Matrix Süresi (mikrosaniye): " + avgMatrixDuration);
                System.out.println();

                
            }
        }
    }

    // Thread kullanarak array işlemlerini yapan ve süreyi ölçen metod
    public static long measureThreadArrayOperations(int[] array) {
        long startTime = System.nanoTime();  //Süreyi ölçme başlangıç

        ThreadArrayOperations threadArrayOperations = new ThreadArrayOperations(array);
        threadArrayOperations.start();

        try {
            threadArrayOperations.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime(); //süre ölçüm bitiş
        return (endTime - startTime) / 1000; 
    }

    // Thread kullanarak matrix işlemlerini yapan ve süreyi ölçen metod
    public static long measureThreadMatrixOperations(int[][] matrix) {
        long startTime = System.nanoTime();

        ThreadMatrixOperations threadMatrixOperations = new ThreadMatrixOperations(matrix);
        threadMatrixOperations.start();

        try { //try catch kısmını raporda detaylıca açıkladım.
            threadMatrixOperations.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000; 
    }

    // Array işlemlerini yapan ve süreyi ölçen metod
    public static long measureArrayOperations(int[] array) {
        long startTime = System.nanoTime();

        calculateArraySum(array); // Toplama işlemi

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000; 
    }

    // Matrix işlemlerini yapan ve süreyi ölçen metod
    public static long measureMatrixOperations(int[][] matrix) {
        long startTime = System.nanoTime();

        transposeMatrix(matrix); // Transpoze alma işlemi
        calculateMatrixSum(matrix); // Matris elemanlarının toplamını alma işlemi

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000; 
    }

    // Array'deki elemanları toplayan metod
    public static void calculateArraySum(int[] array) {
        long sum = 0;
        for (int num : array) {
            sum += num;
        }
    }

    // Matrix transpozunu alan metod
    public static void transposeMatrix(int[][] matrix) {
        int size = matrix.length;
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = matrix[j][i];
            }
        }
    }

    // Matrix elemanlarının toplamını alan metod
    public static void calculateMatrixSum(int[][] matrix) {
        long sum = 0;
        for (int[] row : matrix) {
            for (int num : row) {
                sum += num;
            }
        }
    }

    // Array işlemlerini yapan sınıf
    static class ThreadArrayOperations extends Thread {
        private int[] array;

        public ThreadArrayOperations(int[] array) {
            this.array = array;
        }

   
        public void run() {
            Arrays.sort(array); // Sıralama işlemi
            calculateArraySum(array); // Toplama işlemi
        }

        private void calculateArraySum(int[] array) {
            long sum = 0;
            for (int num : array) {
                sum += num;
            }
        }
    }

    // Matrix işlemlerini yapan sınıf
    static class ThreadMatrixOperations extends Thread {
        private int[][] matrix;

        public ThreadMatrixOperations(int[][] matrix) {
            this.matrix = matrix;
        }

     
        public void run()
        {
            transposeMatrix(matrix); // Transpoze alma işlemi
            calculateMatrixSum(matrix); // Matris elemanlarının toplamını alma işlemi
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

        private void calculateMatrixSum(int[][] matrix) {
            long sum = 0;
            for (int[] row : matrix) {
                for (int num : row) {
                    sum += num;
                }
            }
        }
    }


}

