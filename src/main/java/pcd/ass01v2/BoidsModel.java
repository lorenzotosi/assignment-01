package pcd.ass01v2;

import pcd.ass01v2.monitor.SimulationMonitor;
import pcd.ass01v2.task.CalculateBoidVelocityTask;
import pcd.ass01v2.task.UpdateBoidTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BoidsModel {

    public static final int N_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private final List<Boid> boids;
    private double separationWeight; 
    private double alignmentWeight; 
    private double cohesionWeight; 
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;
    private List<UpdateBoidTask> updateTask;
    private List<CalculateBoidVelocityTask> calculateTask;
    private SimulationMonitor simulationMonitor;
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
        updateTask = new ArrayList<>();
        calculateTask = new ArrayList<>();
    }

    public void setupThreads(final int nboids) {
        boids.clear();
        if (nboids > 0) {
            updateTask.clear();
            calculateTask.clear();
            firstStart = false;
            int nBoidsPerThread = nboids / N_THREADS;
            int poorBoids = nboids % N_THREADS;

            int from = 0;
            int to = nBoidsPerThread - 1;

            for (int i = 0; i < N_THREADS; i++) {
                var b = new ArrayList<Boid>();
                for (int j = from; j <= to; j++) {
                    P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
                    V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
                    b.add(new Boid(pos, vel));
                }

                if (poorBoids != 0) {
                    P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
                    V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
                    b.add(new Boid(pos, vel));
                    poorBoids--;
                }

                calculateTask.add(new CalculateBoidVelocityTask(b, this));
                updateTask.add(new UpdateBoidTask(b, this));
                this.boids.addAll(b);
            }
        }
    }

    public void executeCalculateTask(CountDownLatch ctLatch, ExecutorService executor){
        for (var x : calculateTask) {
            x.addLatch(ctLatch);
            executor.execute(x);
        }
    }

    public void executeUpdateTask(CountDownLatch ctLatch, ExecutorService executor){
        for (var x : updateTask) {
            x.addLatch(ctLatch);
            executor.execute(x);
        }
    }

    public synchronized List<Boid> getBoids(){
    	return List.copyOf(boids);
    }

    public SimulationMonitor getSimulationMonitor() {
        return this.simulationMonitor;
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

    public void resetFirstStart(){
        firstStart = true;
    }
}
