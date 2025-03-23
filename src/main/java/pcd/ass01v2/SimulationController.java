package pcd.ass01v2;

import pcd.ass01v2.Task.BoidTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationController {

    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private BoidsModel boidsModel;
    private CyclicBarrier barrier;

    private List<BoidTask>  boidTaskList = new ArrayList<>();

    public SimulationController(BoidsModel boidsModel) {
        this.boidsModel = boidsModel;

        var boids = boidsModel.getBoids();

        int nThreads = Runtime.getRuntime().availableProcessors() - 1;
        int nBoidsPerThread = boidsModel.getNumBoids() / nThreads;
        int from = 0;
        int to = nBoidsPerThread - 1;

        this.barrier = new CyclicBarrier(nThreads);

        for (int i = 0; i < nThreads; i++) {
            var boidPerTask = new ArrayList<Boid>();
            for(int j = from; j <= to; j++) {
                boidPerTask.add(boids.get(j));
            }
            var task = new BoidTask(boidPerTask, boidsModel, barrier);
            boidTaskList.add(task);
        }

    }

    public void startSimulation() {
        for (int i = 0; i < boidTaskList.size(); i++) {
            var task = boidTaskList.get(i);
            executor.execute(task);
        }
    }

}
