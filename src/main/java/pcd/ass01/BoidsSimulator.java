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
//        int nBoids = model.getThreads().stream().mapToInt(t -> t.getBoids().size()).sum();
//        System.out.println("Number of boids: " + nBoids);
//        model.getThreads().forEach(Thread::start);

        long timer = System.currentTimeMillis();
        int frames = 0;

        while (true) {
            if (this.model.getBarrier() != null) {
                try {
                    this.model.getBarrier().await();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    framerate = frames;
                    frames = 0;
                    timer = System.currentTimeMillis();
                }

                view.ifPresent(boidsView -> boidsView.update(framerate));
            }
        }
    }
}
