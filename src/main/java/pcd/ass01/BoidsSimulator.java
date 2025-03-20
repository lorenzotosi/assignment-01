package pcd.ass01;

import java.util.Optional;

public class BoidsSimulator {

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
        long lastTime = System.nanoTime();

        while (true) {

            if (model.canStart()) {

                long now = System.nanoTime();
                try {
                    this.model.getBarrier().await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //calculate framerate given frametime
                framerate = (int) (1_000_000_000 / (now - lastTime));

                view.ifPresent(boidsView -> boidsView.update(framerate));
                lastTime = now;
            }
        }
    }
}
