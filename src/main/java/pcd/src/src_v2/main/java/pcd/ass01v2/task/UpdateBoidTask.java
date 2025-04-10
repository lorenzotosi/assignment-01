package pcd.ass01v2.task;

import pcd.ass01v2.Boid;
import pcd.ass01v2.BoidsModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UpdateBoidTask implements Runnable {
    private final List<Boid> boids;
    private final BoidsModel boidsModel;
    private CountDownLatch latch;

    public UpdateBoidTask(List<Boid> boids, BoidsModel boidsModel) {
        this.boids = boids;
        this.boidsModel = boidsModel;
    }

    @Override
    public void run() {
        boids.forEach(boid -> boid.updateVelocity(boidsModel));
        boids.forEach(boid -> boid.updatePos(boidsModel));
        latch.countDown();
    }

    public void addLatch(CountDownLatch ctLatch) {
        this.latch = ctLatch;
    }
}
