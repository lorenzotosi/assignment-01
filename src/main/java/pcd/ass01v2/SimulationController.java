package pcd.ass01v2;

import pcd.ass01v2.Task.CalculateBoidVelocityTask;
import pcd.ass01v2.Task.UpdateBoidTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SimulationController {

    private ExecutorService executor;
    private BoidsModel boidsModel;
    private volatile int frameCompleted = 0;
    private boolean isStarted = false;

    private List<CalculateBoidVelocityTask>  boidTaskList = new ArrayList<>();
    private List<UpdateBoidTask> updateBoidTaskList = new ArrayList<>();

    public SimulationController(BoidsModel boidsModel) {
        this.boidsModel = boidsModel;
        this.executor = Executors.newCachedThreadPool();
    }

    public void startSimulation(final int numBoids) {
        boidsModel.setupModel(numBoids);
        var boids = boidsModel.getBoids();
        setupBoidTask(numBoids, boids);
        isStarted = true;
    }

    public synchronized int getAndResetFrameCompleted() {
        int current = frameCompleted;
        frameCompleted = 0;
        return current;
    }

    public void startExecutor() {
        for (CalculateBoidVelocityTask task : boidTaskList) {
            executor.execute(task);
        }

        for (UpdateBoidTask task : updateBoidTaskList) {
            executor.execute(task);
        }
    }


    private void setupBoidTask(int numBoids, List<Boid> boids) {
        int nThreads = Runtime.getRuntime().availableProcessors() - 1;
        int nBoidsPerThread = numBoids / nThreads;
        int poorBoids = numBoids % nThreads;

        int from = 0;
        int to = nBoidsPerThread - 1;

        for (int i = 0; i < nThreads; i++) {
            var boidsPerTask = new ArrayList<Boid>();
            for(int j = from; j <= to; j++) {
                boidsPerTask.add(boids.get((i * nBoidsPerThread) + j));
            }
            if (poorBoids != 0) {
                boidsPerTask.add(boids.get(boids.size() - poorBoids));
                poorBoids--;
            }
            var task = new CalculateBoidVelocityTask(boidsPerTask, this);
            boidTaskList.add(task);
            var updateTask = new UpdateBoidTask(boidsPerTask, boidsModel);
            updateBoidTaskList.add(updateTask);
        }
    }

    public BoidsModel getModel() {
        return boidsModel;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
