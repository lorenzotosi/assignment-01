package pcd.ass01;

import java.util.Optional;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;

    private static final int FRAMERATE = 25;
    private int framerate;

    // Variabili per la misurazione delle performance
    private long totalTime = 0;
    private int count = 0;
    private int blockCounter = 0;

    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
        this.view = Optional.of(view);
    }

    public void runSimulation() {
        while (true) {

            var t0 = System.currentTimeMillis();
            var boids = model.getBoids();

            // Fase 1: aggiornamento velocità
            for (Boid boid : boids) {
                boid.updateVelocity(model);
            }

            // Fase 2: aggiornamento posizione
            for (Boid boid : boids) {
                boid.updatePos(model);
            }

            // Aggiornamento della view e gestione framerate
            if (view.isPresent()) {
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;

                // Logging performance
                totalTime += dtElapsed;
                count++;
                if (count == 100) {
                    long avg = totalTime / count;
                    System.out.println("BoidsSimulator - Block " + blockCounter + " - Avg time per frame: " + avg + " ms");
                    blockCounter++;
                    totalTime = 0;
                    count = 0;
                }

                var framratePeriod = 1000 / FRAMERATE;
                if (dtElapsed < framratePeriod) {
                    try {
                        Thread.sleep(framratePeriod - dtElapsed);
                    } catch (Exception ex) {
                        // Ignora
                    }
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000 / dtElapsed);
                }
            }
        }
    }
}
