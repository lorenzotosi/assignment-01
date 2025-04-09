package pcd.ass01v3.concurrency;

public class MyBarrier {

    private final int nWorkers;
    private int nTotal;

    public MyBarrier(int nWorkers) {
        this.nWorkers = nWorkers;
        this.nTotal = nWorkers;
    }

    public synchronized void await() throws InterruptedException {
        nTotal--;
        if (nTotal > 0) {
            wait();
        } else {
            nTotal = nWorkers;
            notifyAll();
        }
    }
}
