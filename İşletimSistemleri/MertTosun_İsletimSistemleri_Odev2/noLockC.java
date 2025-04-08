import java.util.Random;

class MaxMinBulmaThread extends Thread {
    private int[] satir;
    private volatile int enBuyuk = Integer.MIN_VALUE;
    private volatile int enKucuk = Integer.MAX_VALUE;

    public MaxMinBulmaThread(int[] satir) {
        this.satir = satir;
    }

    // Kritik bölgeye eklediğimiz metotlar

    //en buyuk sayıyı güncelle
    private synchronized void guncelleEnBuyuk(int yeniDeger) {
        if (yeniDeger > enBuyuk)
            enBuyuk = yeniDeger;
    }
    //en kucuk sayıyı güncelle
    private synchronized void guncelleEnKucuk(int yeniDeger) {
        if (yeniDeger < enKucuk)
            enKucuk = yeniDeger;
    }

    // Thread'in çalıştırılacak kod parçası
    public void run() {
        for (int i = 0; i < satir.length; i++) {
            // Kritik bölgeye eklenmiş metodları kullanarak en büyük ve en küçük değerleri güncelle
            guncelleEnBuyuk(satir[i]);
            guncelleEnKucuk(satir[i]);
        }
    }

    // Get metotları
    public int getEnBuyuk() {
        return enBuyuk;
    }

    public int getEnKucuk() {
        return enKucuk;
    }
}

public class noLockC {
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
            threads[i].start();
        }

        // Threadlerin tamamlanmasını bekle ve sonucu ekrana bastır
        int enBuyuk = Integer.MIN_VALUE;
        int enKucuk = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
                // Her bir thread'in bulduğu en büyük ve en küçük değerleri al
                int threadEnBuyuk = threads[i].getEnBuyuk();
                int threadEnKucuk = threads[i].getEnKucuk();
                // Kritik bölgeye girerek ana en büyük ve en küçük değerleri güncelle
                synchronized (noLockC.class) {
                    if (threadEnBuyuk > enBuyuk)
                        enBuyuk = threadEnBuyuk;
                    if (threadEnKucuk < enKucuk)
                        enKucuk = threadEnKucuk;
                }
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
