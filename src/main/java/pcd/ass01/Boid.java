package pcd.ass01;

import java.util.ArrayList;
import java.util.List;

public class Boid {

    private P2d pos;
    private V2d vel;

    private V2d alignment;
    private V2d separation;
    private V2d cohesion;

    public Boid(P2d pos, V2d vel) {
    	this.pos = pos;
    	this.vel = vel;
    }
    
    public P2d getPos() {
    	return pos;
    }

    public V2d getVel() {
    	return vel;
    }
    
    public void calculateVelocity(BoidsModel model) {

        List<Boid> nearbyBoids = getNearbyBoids(model);

        this.separation = calculateSeparation(nearbyBoids, model);
        this.alignment = calculateAlignment(nearbyBoids, model);
        this.cohesion = calculateCohesion(nearbyBoids, model);
    }
    
    public void updateVelocity(BoidsModel model) {
    	this.vel = vel.sum(alignment.mul(model.getAlignmentWeight()))
    			.sum(separation.mul(model.getSeparationWeight()))
    			.sum(cohesion.mul(model.getCohesionWeight()));
        
        /* Limit speed to MAX_SPEED */

        double speed = vel.abs();
        
        if (speed > model.getMaxSpeed()) {
            vel = vel.getNormalized().mul(model.getMaxSpeed());
        }
    }    
    
    public void updatePos(BoidsModel model) {

        /* Update position */

        this.pos = pos.sum(vel);
        
        /* environment wrap-around */
        
        if (pos.x() < model.getMinX()) pos = pos.sum(new V2d(model.getWidth(), 0));
        if (pos.x() >= model.getMaxX()) pos = pos.sum(new V2d(-model.getWidth(), 0));
        if (pos.y() < model.getMinY()) pos = pos.sum(new V2d(0, model.getHeight()));
        if (pos.y() >= model.getMaxY()) pos = pos.sum(new V2d(0, -model.getHeight()));
    }

    private List<Boid> getNearbyBoids(BoidsModel model) {
        var candidates = model.getGrid().getNeighbors(this.pos, model.getPerceptionRadius());
        List<Boid> neighbors = new ArrayList<>();
        for (Boid other : candidates) {
            if (other != null && other != this && pos.distance(other.getPos()) < model.getPerceptionRadius()) {
                neighbors.add(other);
            }
        }
        return neighbors;
    }
    
    private V2d calculateAlignment(List<Boid> nearbyBoids, BoidsModel model) {
        double avgVx = 0;
        double avgVy = 0;
        if (nearbyBoids.size() > 0) {
	        for (Boid other : nearbyBoids) {
	        	V2d otherVel = other.getVel();
	            avgVx += otherVel.x();
	            avgVy += otherVel.y();
	        }	        
	        avgVx /= nearbyBoids.size();
	        avgVy /= nearbyBoids.size();
	        return new V2d(avgVx - vel.x(), avgVy - vel.y()).getNormalized();
        } else {
        	return new V2d(0, 0);
        }
    }

    private V2d calculateCohesion(List<Boid> nearbyBoids, BoidsModel model) {
        double centerX = 0;
        double centerY = 0;
        if (nearbyBoids.size() > 0) {
	        for (Boid other: nearbyBoids) {
	        	P2d otherPos = other.getPos();
	            centerX += otherPos.x();
	            centerY += otherPos.y();
	        }
            centerX /= nearbyBoids.size();
            centerY /= nearbyBoids.size();
            return new V2d(centerX - pos.x(), centerY - pos.y()).getNormalized();
        } else {
        	return new V2d(0, 0);
        }
    }
    
    private V2d calculateSeparation(List<Boid> nearbyBoids, BoidsModel model) {
        double dx = 0;
        double dy = 0;
        int count = 0;
        for (Boid other: nearbyBoids) {
        	P2d otherPos = other.getPos();
    	    double distance = pos.distance(otherPos);
    	    if (distance < model.getAvoidRadius()) {
    	    	dx += pos.x() - otherPos.x();
    	    	dy += pos.y() - otherPos.y();
    	    	count++;
    	    }
    	}
        if (count > 0) {
            dx /= count;
            dy /= count;
            return new V2d(dx, dy).getNormalized();
        } else {
        	return new V2d(0, 0);
        }
    }

}
