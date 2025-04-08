import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

// Yazdırma işlerini temsil eden sınıf
class PrintJob {
    int size; // İş boyutu KB cinsinden

    public PrintJob(int size) {
        this.size = size;
    }
}

// Yazıcı kuyruğunu yöneten sınıf
class PrinterSpooler {
    private final int MAX_BUFFER_SIZE = 2048; // KB Cinsinden bufferın boyutunu oluşturduk 
    private final Queue<PrintJob> buffer = new LinkedList<>(); // Yazdırma işlerini tutan kuyruk
    public int currentBufferSize = 0; // Mevcut kuyrugun boyutu
    
    private final Semaphore mutex = new Semaphore(1); // Eşzamanlı erişimi kontrol etmek için bir mutex semaphore
    private final Semaphore items = new Semaphore(0); //Yazıcı biriktiricisinde bulunan işleri saymak için bir semaphore
    private final Semaphore space; // Yazıcı biriktiricisinde mevcut boş alanı kontrol eden semaphore.

    public PrinterSpooler() {
        space = new Semaphore(MAX_BUFFER_SIZE); // Boş yer semaforu oluştur
    }

    // Yeni bir iş eklendiğinde buffer'a işi ekler ve gerekli izinleri ayarlar.
    public void addJob(PrintJob job) throws InterruptedException { //kesintiye ugrama durumunda InterruptedException(thread durdurma) fırlatabilir.
        space.acquire(job.size); /*   Yeni iş eklenmeden önce, yazıcı biriktiricisinde yeterli boş alan olup olmadığını kontrol eder. 
        . Eğer yeterli boş alan yoksa, bu satırda thread bekletilir. Yeterli alan olduğunda,
         işin boyutu kadar izin alınır ve thread devam eder.   Anlık boş alan 100 KB ise ve eklenmek istenen iş 150 KB ise,
          bu satırda thread bekletilir.(olmasını istediğiniz detay) */
        mutex.acquire(); // Yazıcı biriktiricisine eşzamanlı erişimi kontrol etmek için kilitlenir
        buffer.add(job); // Buffer'a işi ekle
        currentBufferSize += job.size; // iş eklendikten sonra güncel buffer boyutunu güncelliyoruz
        mutex.release(); // Eğer başka bir thread bu semaforu bekliyorsa bu thread'in devam etmesine izin verir.
        items.release(); // Semaphoreun iznini bir artırır. Bu, yazıcı kuyruğunda yeni bir iş bulunduğunu belirtir.
    }

    // Bir iş alındığında buffer'dan işi çıkarır ve gerekli izinleri ayarlar.
    public PrintJob getJob() throws InterruptedException { //kesintiye ugrama durumunda InterruptedException(thread durdurma) fırlatabilir
        items.acquire(); /*kuyruğun dolu olduğunu kontrol eder eğer kuyruğun boş olduğunu
         belirten bir izin yoksa, bu satırda thread bekletilir. Kuyrukta iş varsa izni alır ve devam eder. */
        mutex.acquire(); // buffer'a erişimi kilitler buffer'a yalnızca bir thread'in erişmesine izin verir.
        PrintJob job = buffer.poll(); // metodu, kuyruğun başındaki işi alır ve bu işi kuyruktan çıkarır
        if (job != null) {
            currentBufferSize -= job.size;  // kuyruktan işi aldıysak işin boyutu kadar bufferın boyutunu küçültmeliyiz
            space.release(job.size); /* bu metod iş boyutu kadar boş alanı geri serbest bırakır çünkü yeni işlerin buffera eklenmesi
           için boş alan izni sağlar */
        }
        mutex.release(); // Mutex'i serbest bırak
        return job;
    }
}

// Yazıcıyı temsil eden sınıf
class Printer extends Thread {
    private final PrinterSpooler spooler; // Yazıcı kuyruğu

    public Printer(PrinterSpooler spooler) {
        this.spooler = spooler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                PrintJob job = spooler.getJob();/*Yazıcı kuyruğundan bir iş alır. Bu metot, yazıcı kuyruğunda 
                bir iş bulunana kadar bekler. İş alındığında, job değişkenine atanır. */
                if (job != null) {
                    int printTime = job.size * 10; /*  Yazdırma süresini hesaplar. Her 1 KB, 0.01 saniye (10 milisaniye) sürer.
                     Bu nedenle, işin boyutunu 10 ile çarparak yazdırma süresini milisaniye cinsinden hesaplar.*/
                    Thread.sleep(printTime); // Hesaplanan yazdırma süresi kadar thread'i bekletir. 
                    System.out.println("basılan işin boyutu: " + job.size + "KB");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Kesinti durumu
            }
        }
    }
}

// İş üreten sınıf
class JobGenerator extends Thread {
    private final PrinterSpooler spooler; // Yazıcı kuyruğu

    public JobGenerator(PrinterSpooler spooler) {
        this.spooler = spooler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int jobSize = 50 + (int)(Math.random() * (512 - 50)); /*  Yazıcıya görev gönderen bir thread rastgele bir zamanda 50 – 512 Kb 
                arasında rastgele büyüklüğe sahip bir görevi yazıcı biriktiricisine gönderecektir.*/ 
                PrintJob job = new PrintJob(jobSize); // Yeni iş oluşturduk 
                spooler.addJob(job); // Yazıcı kuyruğuna işi ekle
                System.out.println("eklenen işin boyutu: " + jobSize + "KB"); // 
                Thread.sleep((int)(Math.random() * 1000)); // Rastgele bekleme süresi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Kesinti durumu
            }
        }
    }
}

// Simülasyonu başlatan sınıf
public class PrintSpoolerSimulation {
    public static void main(String[] args) {
        PrinterSpooler spooler = new PrinterSpooler(); // Yazıcı kuyruğu oluştur
        Printer printer = new Printer(spooler); // Yazıcıyı oluştur
        JobGenerator jobGenerator = new JobGenerator(spooler); // İş üreticiyi oluştur

        printer.start(); // Yazıcıyı başlat
        jobGenerator.start(); // İş üreticiyi başlat

        // Örnek olarak işlemleri durdurma örneği göstermek için thread'leri sonlandır
        try {
            Thread.sleep(10000); // 10 saniye çalıştır
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printer.interrupt(); // Yazıcıyı kes
        jobGenerator.interrupt(); // İş üreticiyi kes
    }
}
