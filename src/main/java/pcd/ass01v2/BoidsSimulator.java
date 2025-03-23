package pcd.ass01v2;

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
        long timer = System.currentTimeMillis();
        int frames = 0;
        long frameDuration = 1000 / 200; // Durata di ogni frame in millisecondi per 200 FPS

        while (true) {
            long startTime = System.currentTimeMillis();

            // Simula il comportamento dei boids qui
            // model.updateBoids();

            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                framerate = frames;
                frames = 0;
                timer = System.currentTimeMillis();
            }

            view.ifPresent(boidsView -> boidsView.update(framerate));

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            // Aggiungi un ritardo per controllare la velocità del ciclo
            if (elapsedTime < frameDuration) {
                try {
                    Thread.sleep(frameDuration - elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
