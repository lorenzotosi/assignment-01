package pcd.ass01.worker;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;
import pcd.ass01.monitor.SimulationMonitor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        int iterationCount = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("thread_" + this.getId() + ".txt"))) {
            while (true) {
                simulationMonitor.waitIfSimulationIsStopped();
                try {
                    //long startTime = System.nanoTime();

                    boids.forEach(boid -> boid.calculateVelocity(boidsModel));
                    phase1Barrier.await();
                    boids.forEach(boid -> boid.updateVelocity(boidsModel));
                    boids.forEach(boid -> boid.updatePos(boidsModel));
                    phase2Barrier.await();

//                    long endTime = System.nanoTime();
//                    long iterationTime = endTime - startTime;
//
//                    if (/*iterationCount < 10000*/ false) {
//                        writer.write(iterationTime + "\n");
//                        iterationCount++;
//                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (BrokenBarrierException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
