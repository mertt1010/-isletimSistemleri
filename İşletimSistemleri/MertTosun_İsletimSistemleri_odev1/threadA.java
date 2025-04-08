import java.util.Random;

//THREAD KULLANARAK ARRAYLİ
public class threadA {

    public static void main(String[] args) {
        int n = 200; // Eleman sayısı
        int k = 10;  // oluşturulacak alt küme sayısı
        int[] array = new int[n]; //n eleman sayısına sahip array 
        Random random = new Random(); // rastgele sayılar ürettiğimiz kısım

        
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(10) + 1; // arraye rastgele sayılar (1-10 arası) atıyoruz.
        }

        System.out.println("ARRAY: "); // oluşan arrayi ekrana yazdırıyoruz num:array arrayde dolaşırkem o anda oluşan değeri ekrana yazdırır
        for (int num : array) { 
            System.out.print(num + " ");
        }
        System.out.println();

        
        long startTime = System.nanoTime();// Zaman ölçümü için aralığı başlatıyoruz.

        
        AltKumeThread[] threads = new AltKumeThread[k];// Her bir alt küme için bir thread oluşturuluyor
        for (int i = 0; i < k; i++) {
            int start = i * (n / k); // Alt kümenin başlangıç indeksi
            int end = (i == k - 1) ? n : (i + 1) * (n / k); // Alt kümenin bitiş indeksi

           // thread oluşturuyoruz ve işleme başlıyor
            threads[i] = new AltKumeThread("Thread-" + (i + 1), start, end, array);
            threads[i].start();
        }

       /*  Kodumda bu kısım opsiyonel olarak eklenebilir bu parçayı eklemek işlemler sürerken olası 
           bir kesinti durumunda hatayı izlemek için kullanılanilir.

        for (int i = 0; i < k; i++) {
            try {
                threads[i].join(); 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        }*/

        // zaman aralığını kapatıyoruz
        long endTime = System.nanoTime();

        // Geçen süreyi hesaplama ve mikrosaniyeye çevirme yaptım.
        long duration = (endTime - startTime) / 1000;

        // Toplam süreyi ekrana yazdırıyoruz
        System.out.println("Toplam süre: " + duration + " mikrosaniye");
    }

    // Her bir alt küme için işlemleri yapan Thread sınıfını oluşturuyoruz
    static class AltKumeThread extends Thread {
        private int[] array;
        private int start;
        private int end;
        private int enBuyuk;
        private int enKucuk;
        private int toplam;
        private double ortalama;

        // Constructorlar
        public AltKumeThread(String name, int start, int end, int[] array) {
            super(name); 
            this.start = start;
            this.end = end;
            this.array = array;
        }

        // Thread'i çalıştıracağımız kısım
        public void run() {
            // Kümedeki en büyük sayıyı bulan fonksiyon
            enBuyuk = enBuyukBul(start, end, array);

            // Kümedeki en küçük sayı
            enKucuk = enKucukBul(start, end, array);

            // Kümedeki elemanların toplamı
            toplam = toplamBul(start, end, array);

            // Kümedeki elemanların ortalaması
            ortalama = ortalamayıBul(start, end, array);

            
            System.out.println(getName() + " - En Büyük: " + enBuyuk); //get metoduyla o an ki thread'i döndürüyoruz
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
