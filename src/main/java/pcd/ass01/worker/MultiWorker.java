package pcd.ass01.worker;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.monitor.SimulationMonitor;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MultiWorker extends Thread {

    private final List<Boid> boids;
    private final BoidsModel boidsModel;
    private final CyclicBarrier phase1Barrier;
    private final CyclicBarrier phase2Barrier;
    private final SimulationMonitor simulationMonitor;

    public MultiWorker(List<Boid> boids, BoidsModel boidsModel, CyclicBarrier phase1Barrier,
                       CyclicBarrier phase2Barrier, SimulationMonitor simulationMonitor) {
        this.boids = boids;
        this.boidsModel = boidsModel;
        this.phase1Barrier = phase1Barrier;
        this.phase2Barrier = phase2Barrier;
        this.simulationMonitor = simulationMonitor;
    }

    public void run() {
        double sum = 0.0;
        int count = 0;
        long startTime;
        double elapsedTime;
        while (true) {
            startTime = System.nanoTime();
            simulationMonitor.waitIfSimulationIsStopped();
            try {
                boids.forEach(boid -> boid.calculateVelocity(boidsModel));
                phase1Barrier.await();
                boids.forEach(boid -> boid.updateVelocity(boidsModel));
                boids.forEach(boid -> boid.updatePos(boidsModel));
                phase2Barrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (BrokenBarrierException e) {
                break;
            }
            elapsedTime = (System.nanoTime() - startTime)/1_000_000.0;
            sum += elapsedTime;
            count++;
            if (count == 100) {
                System.out.println(sum / count);
                sum = 0.0;
                count = 0;
            }
        }
    }

}
