import java.util.Random;
//THREAD KULLANMADAN MATRİSLİ
public class threadC2 {

    public static void main(String[] args) {
        int N = 50; 
        int[][] matris = new int[N][N]; // NXN boyutunda matris oluşturduk.
        Random random = new Random();

        // Matrisi rastgele sayılarla dolduralım
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

        // İşlemler
        int[] maxValues = new int[N];
        int[] minValues = new int[N];
        int[] sumValues = new int[N];
        double[] avgValues = new double[N];

        // Her bir satır için işlemleri yapalım
        for (int i = 0; i < N; i++) {
            maxValues[i] = findMax(matris[i]);
            minValues[i] = findMin(matris[i]);
            sumValues[i] = findSum(matris[i]);
            avgValues[i] = findAverage(matris[i]);
        }

        // Sonuçları ekrana yazdıralım
        for (int i = 0; i < N; i++) {
            System.out.println("Satır " + (i+1) + " - En Büyük: " + maxValues[i]);
            System.out.println("Satır " + (i+1) + " - En Küçük: " + minValues[i]);
            System.out.println("Satır " + (i+1) + " - Toplam: " + sumValues[i]);
            System.out.println("Satır " + (i+1) + " - Ortalama: " + avgValues[i]);
            System.out.println();
        }

        long endTime = System.nanoTime(); // Bitiş zamanı
        long totalTime = (endTime - startTime) / 1000;

        System.out.println("Toplam işlem süresi: " + totalTime + " mikrosaniye");
    }

    // En büyük değeri bulan metot
    private static int findMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    // En küçük değeri bulan metot
    private static int findMin(int[] array) {
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    // Toplamı bulan metot
    private static int findSum(int[] array) {
        int sum = 0;
        for (int value : array) {
            sum += value;
        }
        return sum;
    }

    // Ortalamayı bulan metot
    private static double findAverage(int[] array) {
        int sum = findSum(array);
        return (double) sum / array.length;
    }
}
