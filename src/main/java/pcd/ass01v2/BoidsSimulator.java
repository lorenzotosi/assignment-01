package pcd.ass01v2;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class BoidsSimulator {

    private final static int FRAMERATE = 60;
    private BoidsModel model;
    private Optional<BoidsView> view;

    private CountDownLatch countDownLatch;
    
    private int framerate;
    
    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }
      
    public void runSimulation() {
        while (true) {
            countDownLatch = new CountDownLatch(BoidsModel.N_THREADS);
            var t0 = System.currentTimeMillis();
            model.getSimulationMonitor().waitIfSimulationIsStopped();

            try {
                model.execute1(countDownLatch);
                System.out.println("exec1");
                countDownLatch.await();
            } catch (InterruptedException e) {
                System.out.println("Boids simulation interrupted, " + e.getMessage());
            } finally {
                countDownLatch = new CountDownLatch(BoidsModel.N_THREADS);
            }

            try {
                System.out.println("exec2");
                model.execute2(countDownLatch);
                countDownLatch.await();
            } catch (InterruptedException e) {
                System.out.println("Boids simulation interrupted, " + e.getMessage());
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
