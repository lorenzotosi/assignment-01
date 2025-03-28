package pcd.ass01v2.monitor;

public class SimulationMonitor {
    private static final boolean RUNNING = true;
    private static final boolean STOPPED = false;

    private boolean simulationFlag = STOPPED;
    private boolean simulatorActualState = STOPPED;

    public synchronized boolean isSimulationRunning(){
        return this.simulationFlag;
    }

    public synchronized void startSimulation(){
        simulationFlag = RUNNING;
        notifyAll();
    }

    public synchronized void stopSimulation(){
        simulationFlag = STOPPED;
        waitSimulatorToStop();
    }

    private void waitSimulatorToStop() {
        while (simulatorActualState == RUNNING) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted, " + e.getMessage());
            }
        }
    }

    public synchronized void waitIfSimulationIsStopped() {
        while (this.simulationFlag == STOPPED) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted, " + e.getMessage());
            }
        }
    }

    public synchronized void simulatorSafelyStopped() {
        this.simulatorActualState = STOPPED;
        notifyAll();
    }
    public synchronized void simulatorSafelyRunning() {
        this.simulatorActualState = RUNNING;
        notifyAll();
    }
}
