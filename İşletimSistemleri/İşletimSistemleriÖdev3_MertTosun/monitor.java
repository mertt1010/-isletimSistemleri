import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Bir baskı işini temsil eden sınıf
class PrintJob {
    int size; // İşin boyutu 

    // PrintJob sınıfının metodu
    public PrintJob(int size) {
        this.size = size;
    }
}

// Yazıcı kuyruğunu yöneten sınıf
class PrinterSpooler {
    private final int MAX_BUFFER_SIZE = 2048; // Maksimum tampon boyutu 
    private final Queue<PrintJob> buffer = new LinkedList<>(); // İş kuyruğu
    private int currentBufferSize = 0; // Mevcut tampon boyutu

    private final Lock lock = new ReentrantLock(); // ReentrantLock oluşturuyorum
    private final Condition notFull = lock.newCondition(); // Tamponun dolu olmadığı durumu temsil eden condition
    private final Condition notEmpty = lock.newCondition(); // Tamponun boş olmadığı durumu temsil eden condition

    // Yeni bir iş eklemek için kullanılan metot
    public void addJob(PrintJob job) throws InterruptedException {
        lock.lock(); // Kilidi alıyorum
        try {
            while (currentBufferSize + job.size > MAX_BUFFER_SIZE) {
                notFull.await(); // Tampon doluysa, yer açılmasını bekliyorum
            }
            buffer.add(job); // Yeni işi kuyruğa ekliyorum
            currentBufferSize += job.size; // Mevcut tampon boyutunu güncelliyorum
            notEmpty.signal(); // Tamponun boş olmadığını belirtiyorum
        } finally {
            lock.unlock(); // Kilidi serbest bırakıyorum
        }
    }

    // Bir iş almak için kullanılan metot
    public PrintJob getJob() throws InterruptedException {
        lock.lock(); // Kilidi alıyorum
        try {
            while (currentBufferSize == 0) {
                notEmpty.await(); // Tampon boşsa, yeni iş gelmesini bekliyorum
            }
            PrintJob job = buffer.poll(); // Kuyruktan bir iş alıyorum
            if (job != null) {
                currentBufferSize -= job.size; // Mevcut tampon boyutunu güncelliyorum
                notFull.signal(); // Tamponun dolu olmadığını belirtiyorum
            }
            return job; 
        } finally {
            lock.unlock(); // Kilidi serbest bırakıyorum
        }
    }
}

// Yazıcıyı temsil eden iş parçacığı sınıfı
class Printer extends Thread {
    private final PrinterSpooler spooler; // Yazıcı kuyruğu

    // Printer sınıfının yapıcı metodu
    public Printer(PrinterSpooler spooler) {
        this.spooler = spooler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                PrintJob job = spooler.getJob(); // Yazıcı kuyruğundan bir iş alıyorum
                if (job != null) {
                    int printTime = job.size * 10; // İşin boyutuna göre baskı süresini hesaplıyorum
                    Thread.sleep(printTime); // Baskı süresini simüle ediyorum
                    System.out.println("basılan isin boyutu: " + job.size + "KB"); // İşin boyutunu yazdırıyorum
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // İş parçacığının kesinti durumu
            }
        }
    }
}

// İş üreticisini temsil eden iş parçacığı sınıfı
class JobGenerator extends Thread {
    private final PrinterSpooler spooler; // Yazıcı kuyruğu

    // JobGenerator sınıfının yapıcı metodu
    public JobGenerator(PrinterSpooler spooler) {
        this.spooler = spooler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int jobSize = 50 + (int)(Math.random() * (512 - 50)); // Rastgele bir iş boyutu oluşturuyorum
                PrintJob job = new PrintJob(jobSize); // Yeni bir baskı işi oluşturuyorum
                spooler.addJob(job); // Yeni işi yazıcı kuyruğuna ekliyorum
                System.out.println("eklenen isin boyutu: " + jobSize + "KB"); // Eklenen işin boyutunu yazdırıyorum
                Thread.sleep((int)(Math.random() * 1000)); // Rastgele bir süre uyuyorum
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // İş parçacığını kesinti durumu olarak işaretliyorum
            }
        }
    }
}

// Simülasyonun ana sınıfı
public class PrintSpoolerSimulation {
    public static void main(String[] args) {
        PrinterSpooler spooler = new PrinterSpooler(); // Yazıcı kuyruğunu oluşturuyorum
        Printer printer = new Printer(spooler); // Yazıcıyı oluşturuyorum
        JobGenerator jobGenerator = new JobGenerator(spooler); // İş üreticisini oluşturuyorum

        printer.start(); // Yazıcıyı başlatıyorum
        jobGenerator.start(); // İş üreticisini başlatıyorum

        // Simülasyonu belirli bir süre sonra durdurmak için
        try {
            Thread.sleep(10000); // 10 saniye çalıştırıyorum
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printer.interrupt(); // Yazıcı iş parçacığını durdurma
        jobGenerator.interrupt(); // İş üreticisi iş parçacığını durdurma
    }
}
