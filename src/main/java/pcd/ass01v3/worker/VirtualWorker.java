package pcd.ass01v3.worker;

import pcd.ass01v3.Boid;
import pcd.ass01v3.BoidsModel;
import pcd.ass01v3.monitor.SimulationMonitor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class VirtualWorker extends Thread {

    private final Boid boid;
    private final BoidsModel boidsModel;
    private final CyclicBarrier phase1Barrier;
    private final CyclicBarrier phase2Barrier;
    private final SimulationMonitor simulationMonitor;

    public VirtualWorker(Boid boid, BoidsModel boidsModel, CyclicBarrier phase1Barrier,
                         CyclicBarrier phase2Barrier, SimulationMonitor simulationMonitor) {
        this.boid = boid;
        this.boidsModel = boidsModel;
        this.phase1Barrier = phase1Barrier;
        this.phase2Barrier = phase2Barrier;
        this.simulationMonitor = simulationMonitor;
    }

    public void run() {
        while (true) {
            simulationMonitor.waitIfSimulationIsStopped();
            try {
                boid.calculateVelocity(boidsModel);
                phase1Barrier.await();
                boid.updateVelocity(boidsModel);
                boid.updatePos(boidsModel);
                phase2Barrier.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (BrokenBarrierException e) {
                //throw new RuntimeException(e);
                break;
            }
        }

    }

    @Override
    public void interrupt() {
        //this.stopperMonitor.notifyWorkerStop();
        super.interrupt();
    }

}
