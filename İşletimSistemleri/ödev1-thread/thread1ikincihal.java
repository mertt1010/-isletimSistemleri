import java.util.Random;

public class thread1 {

    public static void main(String[] args) {
        int n = 10; // Eleman sayısı
        int k = 2;  // Alt küme sayısı
        int[] array = new int[n]; // N elemanlı array oluşturuldu
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(101) + 1;  // Arraye sayılar(1-10) atadık  
        }

        System.out.println("ARRAY: ");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        // Zaman ölçümü için başlangıç zamanı
        long startTime = System.nanoTime();

        // Her bir alt küme için bir iş parçacığı oluşturuluyor
        AltKumeThread[] threads = new AltKumeThread[k];
        for (int i = 0; i < k; i++) {
            int start = i * (n / k);
            int end = (i == k - 1) ? n : (i + 1) * (n / k);

            threads[i] = new AltKumeThread("Thread-" + (i + 1), start, end, array);
            threads[i].start();
        }

        // İş parçacıklarının bitmesini bekleyelim
        for (int i = 0; i < k; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Zaman ölçümü için bitiş zamanı
        long endTime = System.nanoTime();

        // Geçen süreyi hesaplayalım
        long duration = (endTime - startTime) / 1000000; // Nanosaniyeden milisaniyeye çeviriyoruz

        System.out.println("Toplam süre: " + duration + " milisaniye");
    }

    // Her bir alt küme için işlemleri yapan Thread sınıfı
    static class AltKumeThread extends Thread {
        private int[] array;
        private int start;
        private int end;
        private int enBuyuk;
        private int enKucuk;
        private int toplam;
        private double ortalama;

        public AltKumeThread(String name, int start, int end, int[] array) {
            super(name);
            this.start = start;
            this.end = end;
            this.array = array;
        }

        @Override
        public void run() {
            // Kümedeki en büyük sayıyı bulan fonksiyon
            enBuyuk = enBuyukBul(start, end, array);

            // Kümedeki en küçük sayıyı bulan fonksiyon
            enKucuk = enKucukBul(start, end, array);

            // Kümedeki elemanların toplamını bulan fonksiyon
            toplam = toplamBul(start, end, array);

            // Kümedeki elemanların ortalamasını bulan fonksiyon
            ortalama = ortalamayıBul(start, end, array);

            // Sonuçları ekrana yazdıralım
            System.out.println(getName() + " - En Büyük: " + enBuyuk);
            System.out.println(getName() + " - En Küçük: " + enKucuk);
            System.out.println(getName() + " - Toplam: " + toplam);
            System.out.println(getName() + " - Ortalama: " + ortalama);
            System.out.println();
        }

        // Kümedeki en büyük sayıyı bulan fonksiyon
        private int enBuyukBul(int start, int end, int[] array) {
            int enBuyuk = array[start];
            for (int i = start + 1; i < end; i++) {
                if (array[i] > enBuyuk) {
                    enBuyuk = array[i];
                }
            }
            return enBuyuk;
        }

        // Kümedeki en küçük sayıyı bulan fonksiyon
        private int enKucukBul(int start, int end, int[] array) {
            int enKucuk = array[start];
            for (int i = start + 1; i < end; i++) {
                if (array[i] < enKucuk) {
                    enKucuk = array[i];
                }
            }
            return enKucuk;
        }

        // Kümedeki elemanların toplamını bulan fonksiyon
        private int toplamBul(int start, int end, int[] array) {
            int toplam = 0;
            for (int i = start; i < end; i++) {
                toplam += array[i];
            }
            return toplam;
        }

        // Kümedeki elemanların ortalamasını bulan fonksiyon
        private double ortalamayıBul(int start, int end, int[] array) {
            int toplam = toplamBul(start, end, array);
            return (double) toplam / (end - start);
        }
    }
}
