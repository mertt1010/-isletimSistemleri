import java.util.Random;

public class tekThreadA {
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
                matris[i][j] = random.nextInt(11) - 5; // -5 ile 5 arasında rastgele sayılar atadık
            }
        }

        // Matrisin içinde negatif sayı olup olmadığını kontrol et
        boolean negatifVar = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matris[i][j] < 0) {
                    negatifVar = true;
                    break;
                }
            }
            if (negatifVar) break;
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
            System.out.println();
        }
*/
        if (negatifVar)
            System.out.println("Matrisin içinde negatif sayı bulunmaktadır.");
        else
            System.out.println("Matrisin içinde negatif sayı bulunmamaktadır.");

        System.out.println("Geçen süre: " + gecenZaman + " nanosaniye");
    }
}
