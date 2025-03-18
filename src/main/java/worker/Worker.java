package worker;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {

    private final Boid boid;
    private final BoidsModel boidsModel;
    private final CyclicBarrier barrier;

    public Worker(Boid boid, BoidsModel boidsModel, CyclicBarrier barrier) {
        this.boid = boid;
        this.boidsModel = boidsModel;
        this.barrier = barrier;
    }

    public void run() {
        while (true) {
            try {
                //System.out.println("before");
                boid.calculateVelocity(boidsModel);
                barrier.await();
                boid.updateVelocity(boidsModel);
                boid.updatePos(boidsModel);
                //System.out.println("after");
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
