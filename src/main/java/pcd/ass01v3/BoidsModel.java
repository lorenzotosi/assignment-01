package pcd.ass01;

import pcd.ass01.concurrency.MyBarrier;
import pcd.ass01.monitor.SimulationMonitor;
import pcd.ass01.worker.VirtualWorker;
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
    private List<VirtualWorker> threads;
    private CyclicBarrier phase1Barrier;
    private CyclicBarrier phase2Barrier;
    private final List<VirtualWorker> virtualWorkers;
    private volatile int frameCompleted = 0;
    private final SimulationMonitor simulationMonitor;
    private boolean firstStart = true;
    private final SpatialHashGrid grid;

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
        this.grid = new SpatialHashGrid(perceptionRadius);

    	boids = new CopyOnWriteArrayList<>();
        threads = new ArrayList<>();
    }

    public SpatialHashGrid getGrid() {
        return grid;
    }

    public void setupThreads(final int nBoids) {
        boids.clear();
        if (nBoids > 0) {
            firstStart = false;

            phase1Barrier = new CyclicBarrier(nBoids);
            phase2Barrier = new CyclicBarrier(nBoids, () -> {
                synchronized (this) {
                    frameCompleted++;
                }
                SpatialHashGrid grid = getGrid();
                grid.clear();
                for (Boid boid : getBoids()) {
                    grid.insert(boid);
                }
            });

            for (int i = 0; i < nBoids; i++) {
                P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
                V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
                var b = new Boid(pos, vel);

                var thread = new VirtualWorker(b, this, phase1Barrier, phase2Barrier, simulationMonitor);
                threads.add(thread);
                this.boids.add(b);
            }
        }

    }

    public void stopWorkers() {
        phase2Barrier.reset();
        for (VirtualWorker worker : threads) {
            worker.interrupt();
        }
        for (VirtualWorker worker : threads) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        threads.clear();
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

    public List<VirtualWorker> getThreads(){
        return threads;
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

    public void resetFirstStart() {
        firstStart = true;
    }
}
