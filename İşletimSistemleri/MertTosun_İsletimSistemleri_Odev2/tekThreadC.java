import java.util.Random;

public class tekThreadC {
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

        // En büyük ve en küçük sayıları bul
        int enBuyuk = matris[0][0];
        int enKucuk = matris[0][0];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matris[i][j] > enBuyuk)
                    enBuyuk = matris[i][j];
                if (matris[i][j] < enKucuk)
                    enKucuk = matris[i][j];
            }
        }

        // Bitiş zamanı
        long bitisZamani = System.nanoTime();
        // Geçen zamanı hesapla
        long gecenZaman = bitisZamani - baslangicZamani;

/* 
        System.out.println("Matris:");
        for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          System.out.print(matris[i][j] + "\t");
        }
      }
      System.out.println();
*/
        // Sonuçları ekrana bastır
        System.out.println("Geçen süre: " + gecenZaman + " nanosaniye");
        System.out.println("En büyük sayı: " + enBuyuk);
        System.out.println("En küçük sayı: " + enKucuk);
    }
}
