package pcd.ass01v3.monitor;

public class SimulationMonitor {

    private boolean simulationIsRunning = false;

    public synchronized boolean isSimulationRunning(){
        return simulationIsRunning;
    }

    public synchronized void startSimulation(){
        simulationIsRunning = true;
        notifyAll();
    }

    public synchronized void stopSimulation(){
        simulationIsRunning = false;
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
}
