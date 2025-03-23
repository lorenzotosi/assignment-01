package pcd.ass01v2.Task;

import pcd.ass01v2.Boid;
import pcd.ass01v2.BoidsModel;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BoidTask implements Runnable {
    private final List<Boid> boids;
    private final BoidsModel boidsModel;
    private final CyclicBarrier barrier;

    public BoidTask(List<Boid> boids, BoidsModel boidsModel, CyclicBarrier barrier) {
        this.boids = boids;
        this.boidsModel = boidsModel;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        while (true) {
            try {
                boids.forEach(boid -> boid.calculateVelocity(boidsModel));
                barrier.await();
                boids.forEach(boid -> boid.updateVelocity(boidsModel));
                boids.forEach(boid -> boid.updatePos(boidsModel));
                barrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
