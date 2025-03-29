package pcd.ass01v3;

import java.util.Optional;

public class BoidsSimulator {

    private final static int TARGET_FPS = 20;
    private BoidsModel model;
    private Optional<BoidsView> view;
    
    private int framerate;
    
    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }

    public void runSimulation() {
        long lastSecond = System.currentTimeMillis();
        int frames = 0;

        while (true) {
            long currentTime = System.currentTimeMillis();

            if(currentTime - lastSecond >= 1000) {
                framerate = frames;
                frames = 0;
                lastSecond = currentTime;
            }

            view.ifPresent(boidsView -> boidsView.update(framerate));

            int completed = model.getAndResetFrameCompleted();
            frames += completed;

            try {
                Thread.sleep(1000 / TARGET_FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
