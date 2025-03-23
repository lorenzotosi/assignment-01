package pcd.ass01v2;

import java.util.ArrayList;
import java.util.List;

public class BoidsModel {

    private final List<Boid> boids;
    private double separationWeight;
    private double alignmentWeight;
    private double cohesionWeight;
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;
    private final int numBoids;

    public BoidsModel(int nboids,
                      double initialSeparationWeight,
                      double initialAlignmentWeight,
                      double initialCohesionWeight,
                      double width,
                      double height,
                      double maxSpeed,
                      double perceptionRadius,
                      double avoidRadius) {
        separationWeight = initialSeparationWeight;
        alignmentWeight = initialAlignmentWeight;
        cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        this.numBoids = nboids;

        boids = new ArrayList<>();

        for (int i = 0; i < nboids; i++) {
            P2d pos = new P2d(-width / 2 + Math.random() * width, -height / 2 + Math.random() * height);
            V2d vel = new V2d(Math.random() * maxSpeed / 2 - maxSpeed / 4, Math.random() * maxSpeed / 2 - maxSpeed / 4);
            boids.add(new Boid(pos, vel));
        }


    }

//    public void setupThreads(final int nboids) {
//        int nThreads = Runtime.getRuntime().availableProcessors() - 1;
//        int nBoidsPerThread = nboids / nThreads;
//        int from = 0;
//        int to = nBoidsPerThread - 1;
//
//        this.barrier = new CyclicBarrier(nThreads);
//
//        for (int i = 0; i < nThreads; i++) {
//            var b = new ArrayList<Boid>();
//            for(int j = from; j <= to; j++) {
//                P2d pos = new P2d(-width/2 + Math.random() * width, -height/2 + Math.random() * height);
//                V2d vel = new V2d(Math.random() * maxSpeed/2 - maxSpeed/4, Math.random() * maxSpeed/2 - maxSpeed/4);
//                b.add(new Boid(pos, vel));
//            }
//
//            this.boids.addAll(b);
//        }
//
//    }

    public int getNumBoids() {
        return numBoids;
    }

    public List<Boid> getBoids() {
        return boids;
    }

    public double getMinX() {
        return -width / 2;
    }

    public double getMaxX() {
        return width / 2;
    }

    public double getMinY() {
        return -height / 2;
    }

    public double getMaxY() {
        return height / 2;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public synchronized void setSeparationWeight(double value) {
        this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(double value) {
        this.alignmentWeight = value;
    }

    public synchronized void setCohesionWeight(double value) {
        this.cohesionWeight = value;
    }

    public double getSeparationWeight() {
        return separationWeight;
    }

    public double getCohesionWeight() {
        return cohesionWeight;
    }

    public double getAlignmentWeight() {
        return alignmentWeight;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAvoidRadius() {
        return avoidRadius;
    }

    public double getPerceptionRadius() {
        return perceptionRadius;
    }
}
