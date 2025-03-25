package pcd.ass01v2.task;

import pcd.ass01v2.Boid;
import pcd.ass01v2.BoidsModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CalculateBoidVelocityTask implements Runnable {
    private final List<Boid> boids;
    private final BoidsModel model;
    private CountDownLatch latch;

    public CalculateBoidVelocityTask(List<Boid> boids, BoidsModel model) {
        this.boids = boids;
        this.model = model;

    }

    @Override
    public void run() {
        boids.forEach(boid -> boid.calculateVelocity(model));
        latch.countDown();
    }

    public void addLatch(CountDownLatch ctLatch) {
        this.latch = ctLatch;
    }
}
