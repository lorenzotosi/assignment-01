package pcd.ass01v2.Task;

import pcd.ass01v2.Boid;
import pcd.ass01v2.BoidsModel;
import pcd.ass01v2.SimulationController;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class CalculateBoidVelocityTask implements Runnable {
    private final List<Boid> boids;
    private final SimulationController controller;

    public CalculateBoidVelocityTask(List<Boid> boids, SimulationController controller) {
        this.boids = boids;
        this.controller = controller;

    }

    @Override
    public void run() {
        boids.forEach(boid -> boid.calculateVelocity(controller.getModel()));
//        boids.forEach(boid -> boid.updateVelocity(controller.getModel()));
//        boids.forEach(boid -> boid.updatePos(controller.getModel()));
    }
}
