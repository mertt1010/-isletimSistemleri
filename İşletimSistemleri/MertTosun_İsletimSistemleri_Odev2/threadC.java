import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Her bir satırda en büyük ve en küçük sayıyı bulan thread sınıfı
class MaxMinBulmaThread extends Thread {
    private int[] satir;
    private int enBuyuk = Integer.MIN_VALUE;
    private int enKucuk = Integer.MAX_VALUE;
    private final Lock lock = new ReentrantLock(); // Lock nesnesi

    
    public MaxMinBulmaThread(int[] satir) {
        this.satir = satir;
    }

    // En büyük sayıyı döndüren metot
    public int getEnBuyuk() {
        return enBuyuk;
    }

    // En küçük sayıyı döndüren metot
    public int getEnKucuk() {
        return enKucuk;
    }

    // Thread'in çalıştığı metot
    public void run() {
        for (int i = 0; i < satir.length; i++) {
            // Kritik bölgeye giriş
            lock.lock();
            try {
                if (satir[i] > enBuyuk)
                    enBuyuk = satir[i];
                if (satir[i] < enKucuk)
                    enKucuk = satir[i];
            } finally {
                // Kritik bölgeden çıkış
                lock.unlock();
            }
        }
    }
}

public class threadC {
    
    public static void main(String[] args) {
        // Başlangıç zamanı
        long baslangicZamani = System.nanoTime();

        // NxN'lik matris oluştur
        int N = 8000; // Test için N değeri
        int[][] matris = new int[N][N];

        // Matrisi rastgele sayılarla doldur
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matris[i][j] = random.nextInt(11) - 5; // -5 ile 5 arasında rastgele sayılar
            }
        }

        // Her bir satır için bir thread oluştur
        MaxMinBulmaThread[] threads = new MaxMinBulmaThread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new MaxMinBulmaThread(matris[i]);
            threads[i].start(); // Her bir threadi başlatır
        }

        // Threadlerin tamamlanmasını bekle ve sonucu ekrana bastır
        int enBuyuk = Integer.MIN_VALUE;
        int enKucuk = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join(); // Her bir threadin tamamlanmasını bekle
                // En büyük ve en küçük sayıları güncelle
                if (threads[i].getEnBuyuk() > enBuyuk)
                    enBuyuk = threads[i].getEnBuyuk();
                if (threads[i].getEnKucuk() < enKucuk)
                    enKucuk = threads[i].getEnKucuk();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         /* 
        // Matrisi ekrana bastır
        System.out.println("Matris:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matris[i][j] + "\t");
            }
            System.out.println();
        }*/

        // Bitiş zamanı
        long bitisZamani = System.nanoTime();

        // Çalışma süresini hesapla ve ekrana bastır
        long calismaSuresi = bitisZamani - baslangicZamani;
        System.out.println("Çalışma Süresi: " + calismaSuresi + " nanosaniye");

        // Sonuçları ekrana bastır
        System.out.println("En büyük sayı: " + enBuyuk);
        System.out.println("En küçük sayı: " + enKucuk);
    }
}
