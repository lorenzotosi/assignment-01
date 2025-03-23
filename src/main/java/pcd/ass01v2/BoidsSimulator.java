package pcd.ass01v2;

import java.util.Optional;

public class BoidsSimulator {

    private Optional<BoidsView> view;
    private final static int TARGET_FPS = 165;
    private SimulationController simulationController;

    private int framerate;

    public BoidsSimulator(SimulationController controller) {
        view = Optional.empty();
        simulationController = controller;
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        long lastSecond = System.currentTimeMillis();
        int frames = 0;

        while (true) {
            long currentTime = System.currentTimeMillis();

            frames++;

            if (currentTime - lastSecond >= 1000) {
                framerate = frames;
                frames = 0;
                lastSecond = currentTime;
            }

            view.ifPresent(boidsView -> boidsView.update(framerate));

            int completed = simulationController.getAndResetFrameCompleted();
            frames += completed;

            try {
                Thread.sleep(1000 / TARGET_FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

