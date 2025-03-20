package pcd.ass01;

import java.util.Optional;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;
    
    private static final int FRAMERATE = 60;
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
    	while (true) {
            var t0 = System.currentTimeMillis();
    		/* 
    		 * Improved correctness: first update velocities...
    		 */
//    		for (Boid boid : boids) {
//                boid.updateVelocity(model);
//            }

    		/* 
    		 * ..then update positions
    		 */
//    		for (Boid boid : boids) {
//                boid.updatePos(model);
//            }
            try {
                this.model.getBarrier().await();
            } catch (Exception e) {
                e.printStackTrace();
            }

    		if (view.isPresent()) {
            	view.get().update(framerate);
            	var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var framratePeriod = 1000/FRAMERATE;
                
                if (dtElapsed < framratePeriod) {		
                	try {
                		Thread.sleep(framratePeriod - dtElapsed);
                	} catch (Exception ex) {}
                	framerate = FRAMERATE;
                } else {
                	framerate = (int) (1000/dtElapsed);
                }
    		}
            
    	}
    }
}
