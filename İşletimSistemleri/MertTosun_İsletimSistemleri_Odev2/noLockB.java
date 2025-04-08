import java.util.Random;

class SayiSaymaThread extends Thread {
    private int[] satir;
    private int negatifSayiSayisi = 0;
    private int pozitifSayiSayisi = 0;
    private int sifirSayiSayisi = 0;

    public SayiSaymaThread(int[] satir) {
        this.satir = satir;
    }
  // get set metotları
  //negatif döndürür
    public synchronized int getNegatifSayiSayisi() {
        return negatifSayiSayisi;
    }
 //pozitifi döndürür
    public synchronized int getPozitifSayiSayisi() {
        return pozitifSayiSayisi;
    }
 //sıfırı döndürür
    public synchronized int getSifirSayiSayisi() {
        return sifirSayiSayisi;
    }
 //sayinin durumuna göre poz , neg, sıfır olduğu belirlenir
    public void run() {
        for (int i = 0; i < satir.length; i++) {
            if (satir[i] < 0)
                artirNegatif();
            else if (satir[i] > 0)
                artirPozitif();
            else
                artirSifir();
        }
    }
      //synchroized bu metodun aynı anda sadece bir thread tarafından erişilmesini sağlar race condition engellenir.
    private synchronized void artirNegatif() {
        negatifSayiSayisi++;
    }

    private synchronized void artirPozitif() {
        pozitifSayiSayisi++;
    }

    private synchronized void artirSifir() {
        sifirSayiSayisi++;
    }
}

public class noLockB {
    public static void main(String[] args) {

        long baslangicZamani = System.nanoTime();

        // NxN'lik matris oluştur
        int N = 20; // Test için N değeri
        int[][] matris = new int[N][N];

        // Matrisi rastgele sayılarla doldur
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matris[i][j] = random.nextInt(11) - 5; // -5 ile 5 arasında rastgele sayılar
            }
        }

        // Her bir satır için bir thread oluştur
        SayiSaymaThread[] threads = new SayiSaymaThread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new SayiSaymaThread(matris[i]);
            threads[i].start();
        }

        // Threadlerin tamamlanmasını bekle ve sonucu ekrana bastır
        int toplamNegatif = 0;
        int toplamPozitif = 0;
        int toplamSifir = 0;
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
                toplamNegatif += threads[i].getNegatifSayiSayisi();
                toplamPozitif += threads[i].getPozitifSayiSayisi();
                toplamSifir += threads[i].getSifirSayiSayisi();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Bitiş zamanı
        long bitisZamani = System.nanoTime();
        // Çalışma süresini hesapla ve ekrana bastır
        long calismaSuresi = bitisZamani - baslangicZamani;
        System.out.println("Çalışma Süresi: " + calismaSuresi + " nanosaniye");

        // Sonuçları ekrana bastır
        System.out.println("Toplam negatif sayı sayısı: " + toplamNegatif);
        System.out.println("Toplam pozitif sayı sayısı: " + toplamPozitif);
        System.out.println("Toplam sıfır sayı sayısı: " + toplamSifir);
    }
}
