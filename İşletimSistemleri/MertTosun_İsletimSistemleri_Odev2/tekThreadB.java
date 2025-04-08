import java.util.Random;

public class tekThreadB {
    public static void main(String[] args) {

        // Başlangıç zamanı
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

        // Negatif ve pozitif sayıların sayısını hesapla
        int negatifSayiSayisi = 0;
        int pozitifSayiSayisi = 0;
        int sifirsayisi = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matris[i][j] < 0)
                    negatifSayiSayisi++;
                else if (matris[i][j] > 0)
                    pozitifSayiSayisi++;
                    else {
                    sifirsayisi++;
                    }
            }
        }
        // Bitiş zamanı
        long bitisZamani = System.nanoTime();
        // Geçen zamanı hesapla
        long gecenZaman = bitisZamani - baslangicZamani;

        System.out.println("Matris:");
        for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          System.out.print(matris[i][j] + "\t");
        }
      }
      System.out.println();

        // Sonuçları ekrana bastır
        System.out.println("Geçen süre: " + gecenZaman + " nanosaniye");
        System.out.println("Toplam negatif sayı sayısı: " + negatifSayiSayisi);
        System.out.println("Toplam pozitif sayı sayısı: " + pozitifSayiSayisi);
        System.out.println("Toplam sıfır sayısı: " + sifirsayisi);
    }
}
