import java.util.Random;
//THREAD KULLANARAK MATRİSLİ
public class threadB {

    public static void main(String[] args) {
        int N = 50; // Matris boyutunu belirliyoruz(NXN)
        int[][] matris = new int[N][N]; // N x N boyutunda matris oluşturduk
        Random random = new Random(); //random sayılar ürettik

        // Matrisi rastgele sayılarla dolduruyoruz
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matris[i][j] = random.nextInt(10) + 1; // 1-10 arası rastgele sayılar
            }
        }

        // Matrisi ekrana yazdıralım
        System.out.println("Matris:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matris[i][j] + " ");
            }
            System.out.println();
        }

        long startTime = System.nanoTime(); // Başlangıç zamanı

        // Her bir satır için bir Thread oluşturup işlemleri yaptıralım
        for (int i = 0; i < N; i++) {
            RowThread rowThread = new RowThread(i, matris[i]);
            rowThread.start();
        }
        long endTime = System.nanoTime(); // Bitiş zamanı
        long totalTime = (endTime - startTime)/1000; // Toplam süre

        System.out.println("Toplam işlem süresi: " + totalTime + " mikrosaniye");
    }

    // Her bir satır için Thread sınıfı oluşturuyoruz
    static class RowThread extends Thread {
        private int[] row;
        private int max;
        private int min;
        private int sum;
        private double avg;

        public RowThread(int index, int[] row) { //kaçıncı satırın threadi olduğunu belirtmek için constructor
            super("RowThread-" + index);
            this.row = row;
        }

        public void run() {
            //  max , min , toplam ve ortalama işlemlerini yaptırıyoruz
            max = findMax(row);
            min = findMin(row);
            sum = findSum(row);
            avg = findAverage(row);

            // Sonuçları ekrana yazdıralım
            System.out.println(getName() + " - En Büyük: " + max);
            System.out.println(getName() + " - En Küçük: " + min);
            System.out.println(getName() + " - Toplam: " + sum);
            System.out.println(getName() + " - Ortalama: " + avg);
            System.out.println();
        }

        // En büyük değeri bulan metot
        private int findMax(int[] array) {
            int max = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
            return max;
        }

        // En küçük değeri bulan metot
        private int findMin(int[] array) {
            int min = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
            return min;
        }

        // Toplamı bulan metot
        private int findSum(int[] array) {
            int sum = 0;
            for (int value : array) {
                sum += value;
            }
            return sum;
        }

        // Ortalamayı bulan metot
        private double findAverage(int[] array) {
            int sum = findSum(array);
            return (double) sum / array.length;
        }
    }
}
