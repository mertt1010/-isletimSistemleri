import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Her bir satırda sayıları sayan thread sınıfı
class SayiSaymaThread extends Thread {
    private int[] satir;
    private int negatifSayiSayisi = 0;
    private int pozitifSayiSayisi = 0;
    private int sifirSayiSayisi = 0;
    private final Lock lock = new ReentrantLock(); // Lock nesnesi

    
    public SayiSaymaThread(int[] satir) {
        this.satir = satir;
    }

    // Negatif sayı sayısını döndüren metot
    public int getNegatifSayiSayisi() {
        return negatifSayiSayisi;
    }

    // Pozitif sayı sayısını döndüren metot
    public int getPozitifSayiSayisi() {
        return pozitifSayiSayisi;
    }

    // Sıfır sayısı döndüren metot
    public  int getSifirSayiSayisi() {
        return sifirSayiSayisi;
    }


    // Thread'in çalıştığı metot
    public void run() {
        for (int i = 0; i < satir.length; i++) {
            // Kritik bölgeye giriş(raporda kritik bölgeden bahsettim)
            lock.lock();
            try {
                if (satir[i] < 0)
                    negatifSayiSayisi++;
                else if (satir[i] > 0)
                    pozitifSayiSayisi++;
                else {
                    sifirSayiSayisi++;
                }
            } finally {
                // Kritik bölgeden çıkış
                lock.unlock();
            }
        }
    }
}

public class threadB {

    public static void main(String[] args) {
        // Başlangıç zamanı
        long baslangicZamani = System.nanoTime();

        // NxN'lik matris oluştur
        int N = 20; // Test için N değeri
        int[][] matris = new int[N][N];

        // Matrisi rastgele sayılarla doldurdık
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matris[i][j] = random.nextInt(11) - 5; // -5 ile 5 arası rastgele atadık
            }
        }

        // Her bir satır için bir thread oluştur
        SayiSaymaThread[] threads = new SayiSaymaThread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new SayiSaymaThread(matris[i]);
            threads[i].start(); // Her bir threadi başlatır
        }

        // Threadlerin tamamlanmasını bekle ve sonucu ekrana bastır
        int toplamNegatif = 0;
        int toplamPozitif = 0;
        int sifirsayisi = 0;
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join(); // Her bir threadin tamamlanmasını bekle
                // Toplam sayıları topla
                toplamNegatif += threads[i].getNegatifSayiSayisi();
                toplamPozitif += threads[i].getPozitifSayiSayisi();
                sifirsayisi += threads[i].getSifirSayiSayisi();
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
        System.out.println("Toplam negatif sayı sayısı: " + toplamNegatif);
        System.out.println("Toplam pozitif sayı sayısı: " + toplamPozitif);
        System.out.println("Toplam sıfır sayısı: " + sifirsayisi);
    }
}
