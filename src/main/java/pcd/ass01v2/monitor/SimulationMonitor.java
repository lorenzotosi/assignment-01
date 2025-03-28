package pcd.ass01v2.monitor;

public class SimulationMonitor {

    private boolean simulationIsRunning = false;
    private boolean simulatorStopped = true;

    public synchronized boolean isSimulationRunning(){
        return simulationIsRunning;
    }

    public synchronized void startSimulation(){
        simulationIsRunning = true;
        notifyAll();
    }

    public synchronized void stopSimulation(){
        simulationIsRunning = false;
        waitSimulatorStop();
    }

    private void waitSimulatorStop() {
        while (!simulatorStopped) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted, " + e.getMessage());
            }
        }
    }

    public synchronized void waitIfSimulationIsStopped() {
        while (!this.simulationIsRunning) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted, " + e.getMessage());
            }
        }
    }

    public synchronized void simulatorStopped() {
        this.simulatorStopped = true;
        notifyAll();
    }
    public synchronized void simulatorRunning() {
        this.simulatorStopped = false;
        notifyAll();
    }
    public synchronized boolean isSimulatorStopped(){
        return this.simulatorStopped;
    }
}
