import java.util.Random;

public class thread1 {

    public static void main(String[] args) {

        int n = 10; // Eleman sayısı
        int k = 5;  // Alt küme sayısı
        int[] array = new int[n]; // N elemanlı array oluşturdum
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(101) + 1;  // Arraye sayılar(1-10) atadık  
        }

        System.out.println("ARRAY: ");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();

        int start, end;
        int[] akeb = new int[k]; // Alt kume en buyukler
        int[] akek = new int[k]; // Alt kume en kucukler
        int[] aktop = new int[k]; // Alt kume toplamlar
        int[] akort = new int[k]; // Alt kume ortalamalar

        for (int i = 0; i < k; i++) {
            start = i * (n / k);
            end = (i == k - 1) ? n : (i + 1) * (n / k);

            akeb[i] = enbuyukbul(start, end, array);
            akek[i] = enkucukbul(start, end, array);
            aktop[i] = toplambul(start, end, array);
            akort[i] = ortalamabul(start, end, array);
        }

        // Sonuçları ekrana yazdırıyoruz
        System.out.println("Alt Kümenlerin en büyükleri: ");
        for (int num : akeb) {
            System.out.print(num + " ");
        }
        System.out.println();

        System.out.println("Alt Kümelerin en Küçükleri: ");
        for (int num : akek) {
            System.out.print(num + " ");
        }
        System.out.println();

        System.out.println("Alt kümelerin toplamları: ");
        for (int num : aktop) {
            System.out.print(num + " ");
        }
        System.out.println();

        System.out.println("Alt Kumelerin Ortalamaları: ");
        for (double num : akort) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // Kümedeki en büyük sayıyı bulan fonksiyon
    public static int enbuyukbul(int start, int end, int[] array) {
        int enbuyuk = array[start];
        for (int i = start + 1; i < end; i++) {
            if (array[i] > enbuyuk) {
                enbuyuk = array[i];
            }
        }
        return enbuyuk;
    }

    // Kümedeki en küçük sayıyı bulan fonksiyon
    public static int enkucukbul(int start, int end, int[] array) {
        int enkucuk = array[start];
        for (int i = start + 1; i < end; i++) {
            if (array[i] < enkucuk) {
                enkucuk = array[i];
            }
        }
        return enkucuk;
    }

    // Kümedeki elemanların toplamını bulan fonksiyon
    public static int toplambul(int start, int end, int[] array) {
        int toplam = 0;
        for (int i = start; i < end; i++) {
            toplam = toplam + array[i];
        }
        return toplam;
    }

    // Kümedeki elemanların ortalamasını bulan fonksiyon
    public static int ortalamabul(int start, int end, int[] array) {
        double toplam = 0;
        for (int i = start; i < end; i++) {
            toplam = toplam + array[i];
        }
        return (int) toplam / (end - start);
    }
     
    }


