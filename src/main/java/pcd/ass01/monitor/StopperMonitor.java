package pcd.ass01.monitor;

public class StopperMonitor {

    private final int nThreads;
    private volatile int workerSafelyStopped;

    public StopperMonitor(final int nThreads) {
        this.nThreads = nThreads;
        this.workerSafelyStopped = 0;
    }

    public synchronized void waitWorkersEnd() {
        while (workerSafelyStopped < nThreads) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("StopperMonitor: " + e.getMessage());
            }
        }
    }

    public synchronized void notifyWorkerStop() {
        this.workerSafelyStopped++;
    }

}
