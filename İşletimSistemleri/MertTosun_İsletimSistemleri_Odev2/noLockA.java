import java.util.Random;

class NegatifBulmaThread extends Thread {
    private int[] satir;
    private boolean negatifVar = false;

    public NegatifBulmaThread(int[] satir) {
        this.satir = satir;
    }

    public boolean isNegatifVar() {
        return negatifVar;
    }
    /*Run metodu içindeki kritik bölüm,
     sadece bir thread tarafından aynı anda erişilebilir hale gelir, böylece race condition engellenir. */
    public void run() {
        // Kritik bölümü synchronized anahtar kelimesiyle koruyoruz
        synchronized (this) {
            for (int i = 0; i < satir.length; i++) {
                if (satir[i] < 0) {
                    negatifVar = true;
                    return; // Negatif sayı bulunduğunda diğer işlemleri durdur
                }
            }
        }
    }
}

public class noLockA {

    public static void main(String[] args) {
        // Başlangıç zamanı
        long baslangicZamani = System.nanoTime();

        // NxN'lik matris oluştur
        int N = 4; // Test için N değeri
        int[][] matris = new int[N][N];

        // Matrisi rastgele sayılarla doldur
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matris[i][j] = random.nextInt(11) - 5; // -5 ile 5 arasında rastgele sayılar atadık
            }
        }

        // Her bir satır için bir thread oluştur
        NegatifBulmaThread[] threads = new NegatifBulmaThread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new NegatifBulmaThread(matris[i]);
            threads[i].start();
        }

        // Threadlerin tamamlanmasını bekle ve sonucu ekrana bastır
        boolean negatifVar = false;
        for (int i = 0; i < N; i++) {
            try {
                threads[i].join();
                if (threads[i].isNegatifVar()) {
                    negatifVar = true;
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       /* BU KISIMLARI PERFORMANS TESTİ YAPARKEN HEM MATRİSİ YAZDIRARAK HEMDE YAZDIRMADAN DENEDİM HOCAM YAZDIRMA KISMI PERFORMANSA ETKİLEYECEGİNİ
       SANMIYORUM AMA YİNE DE BİR BAKMAK İSTEDİM.
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

        if (negatifVar)
            System.out.println("Matrisin içinde negatif sayı bulunmaktadır.");
        else
            System.out.println("Matrisin içinde negatif sayı bulunmamaktadır.");
    }
}
