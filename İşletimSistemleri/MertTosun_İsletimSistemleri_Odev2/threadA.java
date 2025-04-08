import java.util.Random;
import java.util.concurrent.locks.Lock;//LOCK MEKANİZMASININ KULLANIMI İÇİN GEREKLİ KÜTÜPHANELER
import java.util.concurrent.locks.ReentrantLock;

// Her bir satırda negatif sayı arayan thread sınıfı
class NegatifBulThread extends Thread {
    private int[] satir;
    private boolean negatifVar = false; // Negatif sayı bulunduğunu gösteren bayrak
    private final Lock lock = new ReentrantLock(); // Lock nesnesi

    // Constructor
    public NegatifBulThread(int[] satir) {
        this.satir = satir;
    }

    // Negatif sayı bulunup bulunmadığını döndüren metot
    public boolean isNegatifVar() {
        return negatifVar;
    }

    // Thread'in çalıştığı metot
    public void run() {
        // Matris satırını tarar ve negatif sayı bulursa bayrağı true yapar
        for (int i = 0; i < satir.length; i++) {
            if (satir[i] < 0) {
                // Lock kullanarak kritik bölgeye erişimi korur
                lock.lock();
                try {
                    negatifVar = true;
                    return; // Negatif sayı bulunduğunda diğer işlemleri durdur
                } finally {
                    lock.unlock(); // Lock'I serbest bırak
                }
            }
        }
    }
}


public class threadA {

    public static void main(String[] args) {
        // Başlangıç zamanı
        long baslangicZamani = System.nanoTime();

        // NxN'lik matris oluşturduk
        int N = 4; // Test için N değerimiz
        int[][] matris = new int[N][N];

        // Matrisi rastgele sayılarla dolduruyrouz
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matris[i][j] = random.nextInt(11) - 5; // -5 ile 5 arasında rastgele sayılar atadık
            }
        }

        // Her bir satır için bir thread oluştur
        NegatifBulThread[] threads = new NegatifBulThread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new NegatifBulThread(matris[i]);
            threads[i].start(); // Her bir threadi başlat
        }

        // Threadlerin tamamlanmasını bekle ve sonucu ekrana yaz
        boolean negatifVar = false;
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join(); // Her bir threadin tamamlanmasını bekle
                if (threads[i].isNegatifVar()) {
                    negatifVar = true;
                    break;
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
        }
*/
        // Bitiş zamanı
        long bitisZamani = System.nanoTime();

        // Çalışma süresini hesapla ve ekrana bastır
        long calismaSuresi = bitisZamani - baslangicZamani;
        System.out.println("Çalışma Süresi: " + calismaSuresi + " nanosaniye");

        // Negatif sayı bulunup bulunmadığını ekrana bastır
        if (negatifVar)
            System.out.println("Matrisin içinde negatif sayı bulunmaktadır.");
        else
            System.out.println("Matrisin içinde negatif sayı bulunmamaktadır.");
    }
}
