package pcd.ass01.worker;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MultiWorker extends Thread {

    private final List<Boid> boids;
    private final BoidsModel boidsModel;
    private final CyclicBarrier phase1Barrier;
    private final CyclicBarrier phase2Barrier;

    public MultiWorker(List<Boid> boids, BoidsModel boidsModel, CyclicBarrier phase1Barrier, CyclicBarrier phase2Barrier) {
        this.boids = boids;
        this.boidsModel = boidsModel;
        this.phase1Barrier = phase1Barrier;
        this.phase2Barrier = phase2Barrier;
    }

    public List<Boid> getBoids() {
        return boids;
    }

    public void run() {
        while (true) {
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
                throw new RuntimeException(e);
            }
        }

    }

}
