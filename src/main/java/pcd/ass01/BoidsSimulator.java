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
        int nBoids = model.getThreads().stream().mapToInt(t -> t.getBoids().size()).sum();
        System.out.println("Number of boids: " + nBoids);
        model.getThreads().forEach(Thread::start);

        long lastTime = System.nanoTime();

        while (true) {
            long now = System.nanoTime();
            try {
                this.model.getBarrier().await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //calculate framerate given frametime
            framerate = (int) (1_000_000_000 / (now - lastTime));

            if (view.isPresent()) {
                view.get().update(framerate);
            }
            lastTime = now;
        }
    }
}
