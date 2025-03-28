package pcd.ass01.concurrency;

public class MyBarrier {

    private int nWorkers;
    private int nTotal = 0;

    public MyBarrier(int nWorkers) {
        this.nWorkers = nWorkers;
    }

    public synchronized void await() throws InterruptedException {
        nTotal++;
        if(nTotal == nWorkers) {
            notifyAll();
        } else {
            while (nTotal < nWorkers) {
                wait();
            }
        }
    }
}
