import java.util.Random;
//THREAD KULLANMADAN ARRAYLİ
public class threadC {

    public static void main(String[] args) {
        int N = 200; 
        int M = 10; 
        int[] array = new int[N]; 
        Random random = new Random();

        // Array'e rastgele değerler atıyoruz
        for (int i = 0; i < N; i++) {
            array[i] = random.nextInt(10) + 1; // 1 ile 10 arasında rastgele sayılar
        }

        System.out.println("Array: ");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        long startTime = System.nanoTime(); // Başlangıç zamanı

        int start, end;
        for (int i = 0; i < M; i++) {
            start = i * (N / M);
            end = (i == M - 1) ? N : (i + 1) * (N / M);
            
            // Alt küme için işlemleri yapıyoruz
            int altKumeBuyuk = findMax(array, start, end);
            int altKumeKucuk = findMin(array, start, end);
            int altKumeToplam = findSum(array, start, end);
            double altKumeOrtalama = findAverage(array, start, end);

            // Sonuçları yazdır
            System.out.println("Alt Kume " + (i+1) + " Buyuk: " + altKumeBuyuk);
            System.out.println("Alt Kume " + (i+1) + " Kucuk: " + altKumeKucuk);
            System.out.println("Alt Kume " + (i+1) + " Toplam: " + altKumeToplam);
            System.out.println("Alt Kume " + (i+1) + " Ortalama: " + altKumeOrtalama);
            System.out.println();
        }

        long endTime = System.nanoTime(); // Bitiş zamanı
        long totalTime = (endTime - startTime) / 1000; // Toplam süre

        System.out.println("Toplam işlem süresi: " + totalTime + " mikrosaniye");
    }

    // Alt kümedeki en büyük sayıyı bulan fonksiyon
    public static int findMax(int[] array, int start, int end) {
        int max = array[start];
        for (int i = start + 1; i < end; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    // Alt kümedeki en küçük sayıyı bulan fonksiyon
    public static int findMin(int[] array, int start, int end) {
        int min = array[start];
        for (int i = start + 1; i < end; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    // Alt kümedeki elemanların toplamını bulan fonksiyon
    public static int findSum(int[] array, int start, int end) {
        int sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }

    // Alt kümedeki elemanların ortalamasını bulan fonksiyon
    public static double findAverage(int[] array, int start, int end) {
        int sum = findSum(array, start, end);
        return (double) sum / (end - start);
    }
}
