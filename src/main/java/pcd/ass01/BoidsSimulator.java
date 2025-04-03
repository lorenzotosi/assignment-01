package pcd.ass01;

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
      
//    public void runSimulation() {
////        int nBoids = model.getThreads().stream().mapToInt(t -> t.getBoids().size()).sum();
////        System.out.println("Number of boids: " + nBoids);
////        model.getThreads().forEach(Thread::start);
//
//        long timer = System.currentTimeMillis();
//        int frames = 0;
//
//        while (true) {
//            if (this.model.getBarrier() != null) {
//                try {
//                    this.model.getBarrier().await();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                frames++;
//
//                if (System.currentTimeMillis() - timer > 1000) {
//                    framerate = frames;
//                    frames = 0;
//                    timer = System.currentTimeMillis();
//                }
//
//                view.ifPresent(boidsView -> boidsView.update(framerate));
//            }
//        }
//    }

    public void runSimulation() {
        long lastSecond = System.currentTimeMillis();
        int frames = 0;

        while (true) {
            long currentTime = System.currentTimeMillis();

            // Simula il comportamento dei boids qui
            // model.updateBoids();

            SpatialHashGrid grid = model.getGrid();
            grid.clear();
            for (Boid boid : model.getBoids()) {
                grid.insert(boid);
            }

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
