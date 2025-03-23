package pcd.ass01v2;

import pcd.ass01v2.Task.BoidTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationController {

    private ExecutorService executor;
    private BoidsModel boidsModel;
    private CyclicBarrier barrier;

    private List<BoidTask>  boidTaskList = new ArrayList<>();

    public SimulationController(BoidsModel boidsModel) {
        this.boidsModel = boidsModel;
        this.executor = Executors.newCachedThreadPool();
    }

    public void startSimulation(final int numBoids) {
        boidsModel.setupModel(numBoids);
        var boids = boidsModel.getBoids();
        setupBoidTask(numBoids, boids);
        startExecutor();
    }

    private void startExecutor() {
        for (BoidTask task : boidTaskList) {
            executor.execute(task);
        }
    }

    private void setupBoidTask(int numBoids, List<Boid> boids) {
        int nThreads = Runtime.getRuntime().availableProcessors() - 1;
        int nBoidsPerThread = numBoids / nThreads;
        int from = 0;
        int to = nBoidsPerThread - 1;

        this.barrier = new CyclicBarrier(nThreads);

        for (int i = 0; i < nThreads; i++) {
            var boidsPerTask = new ArrayList<Boid>();
            for(int j = from; j <= to; j++) {
                boidsPerTask.add(boids.get(j));
            }
            var task = new BoidTask(boidsPerTask, boidsModel, barrier);
            boidTaskList.add(task);
        }
    }

    public BoidsModel getModel() {
        return boidsModel;
    }
}
