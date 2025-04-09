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
    private volatile boolean running = true;

    public void stopWorker() {
        running = false;
    }

    public MultiWorker(List<Boid> boids, BoidsModel boidsModel, CyclicBarrier phase1Barrier,
                       CyclicBarrier phase2Barrier, SimulationMonitor simulationMonitor) {
        this.boids = boids;
        this.boidsModel = boidsModel;
        this.phase1Barrier = phase1Barrier;
        this.phase2Barrier = phase2Barrier;
        this.simulationMonitor = simulationMonitor;
    }

    public void run() {
        while (running) {
            simulationMonitor.waitIfSimulationIsStopped();
            if (!running) break;
            try {
                // Phase 1: Calculate velocities
                boids.forEach(boid -> boid.calculateVelocity(boidsModel));
                if (!running) break;
                phase1Barrier.await();
                // Phase 2: Update velocities and positions
                boids.forEach(boid -> boid.updateVelocity(boidsModel));
                boids.forEach(boid -> boid.updatePos(boidsModel));
                if (!running) break;
                phase2Barrier.await();
            } catch (BrokenBarrierException e) {
                if (!running) {
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        Thread.currentThread().interrupt();
    }

}
