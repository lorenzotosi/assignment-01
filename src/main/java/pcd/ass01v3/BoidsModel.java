package pcd.ass01v3;

import pcd.ass01v3.monitor.SimulationMonitor;
import pcd.ass01v3.worker.VirtualWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;

public class BoidsModel {
    
    private final List<Boid> boids;
    private double separationWeight; 
    private double alignmentWeight; 
    private double cohesionWeight; 
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;
    private final List<VirtualWorker> virtualWorkers;
    private volatile int frameCompleted = 0;
    private final SimulationMonitor simulationMonitor;
    private boolean firstStart = true;

    public BoidsModel(int nboids,
                      double initialSeparationWeight,
                      double initialAlignmentWeight,
                      double initialCohesionWeight,
                      double width,
                      double height,
                      double maxSpeed,
                      double perceptionRadius,
                      double avoidRadius,
                      SimulationMonitor simulationMonitor) {
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        this.simulationMonitor = simulationMonitor;
        
    	boids = new CopyOnWriteArrayList<>();
        virtualWorkers = new ArrayList<>();
    }

    public void stopSimulation() {
        virtualWorkers.forEach(VirtualWorker::stopGracefully);

        virtualWorkers.forEach(thread -> {;
            try {
                thread.join(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        virtualWorkers.clear();
        boids.clear();
        firstStart = true;
    }

    public void setupThreads(final int nboids) {
        firstStart = false;
        CyclicBarrier phase1Barrier = new CyclicBarrier(nboids);
        CyclicBarrier phase2Barrier = new CyclicBarrier(nboids, () -> {
            synchronized (this) {
                frameCompleted++;
            }
        });

        for (int i = 0; i < nboids; i++) {
            P2d pos = new P2d(-width/2 + Math.random() * width, -height/2 + Math.random() * height);
            V2d vel = new V2d(Math.random() * maxSpeed/2 - maxSpeed/4, Math.random() * maxSpeed/2 - maxSpeed/4);
            var b = new Boid(pos, vel);
            boids.add(b);
            var thread = new VirtualWorker(b, this, phase1Barrier, phase2Barrier, simulationMonitor);
            virtualWorkers.add(thread);
        }
    }

    public synchronized int getAndResetFrameCompleted() {
        int current = frameCompleted;
        frameCompleted = 0;
        return current;
    }

    public List<Boid> getBoids(){
    	return boids;
    }

    public SimulationMonitor getSimulationMonitor() {
        return this.simulationMonitor;
    }

    public List<VirtualWorker> getVirtualWorkers(){
        return virtualWorkers;
    }
    
    public double getMinX() {
    	return -width/2;
    }

    public double getMaxX() {
    	return width/2;
    }

    public double getMinY() {
    	return -height/2;
    }

    public double getMaxY() {
    	return height/2;
    }
    
    public double getWidth() {
    	return width;
    }
 
    public double getHeight() {
    	return height;
    }

    public synchronized void setSeparationWeight(double value) {
    	this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(double value) {
    	this.alignmentWeight = value;
    }

    public synchronized void setCohesionWeight(double value) {
    	this.cohesionWeight = value;
    }

    public double getSeparationWeight() {
    	return separationWeight;
    }

    public double getCohesionWeight() {
    	return cohesionWeight;
    }

    public double getAlignmentWeight() {
    	return alignmentWeight;
    }
    
    public double getMaxSpeed() {
    	return maxSpeed;
    }

    public double getAvoidRadius() {
    	return avoidRadius;
    }

    public double getPerceptionRadius() {
    	return perceptionRadius;
    }

    public boolean isFirstStart() {
        return firstStart;
    }
}
