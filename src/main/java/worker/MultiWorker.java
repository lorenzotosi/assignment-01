package worker;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MultiWorker extends Thread {

    private final List<Boid> boids;
    private final BoidsModel boidsModel;
    private final CyclicBarrier barrier;

    public MultiWorker(List<Boid> boids, BoidsModel boidsModel, CyclicBarrier barrier) {
        this.boids = boids;
        this.boidsModel = boidsModel;
        this.barrier = barrier;
    }

    public List<Boid> getBoids() {
        return boids;
    }

    public void run() {
        while (true) {
            try {
                boids.forEach(boid -> {
                    //System.out.println("before");
                    boid.calculateVelocity(boidsModel);
                });
                barrier.await();
                boids.forEach(boid -> {
                    //System.out.println("before2");
                    boid.updateVelocity(boidsModel);
                });
                boids.forEach(boid -> {
                    //System.out.println("after");
                    boid.updatePos(boidsModel);
                });
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
