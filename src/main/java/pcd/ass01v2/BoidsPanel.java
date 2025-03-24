package pcd.ass01v2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class BoidsPanel extends JPanel {

	private BoidsView view;
	private SimulationController controller;
    private int framerate;

    public BoidsPanel(BoidsView view, SimulationController controller) {
    	this.controller = controller;
    	this.view = view;
    }

    public void setFrameRate(int framerate) {
    	this.framerate = framerate;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        
        var w = view.getWidth();
        var h = view.getHeight();
        var envWidth = controller.getModel().getWidth();
        var xScale = w/envWidth;
        // var envHeight = model.getHeight();
        // var yScale = h/envHeight;

        var boids = controller.getModel().getBoids();

        g.setColor(Color.BLUE);
        for (Boid boid : boids) {
        	var x = boid.getPos().x();
        	var y = boid.getPos().y();
        	int px = (int)(w/2 + x*xScale);
        	int py = (int)(h/2 - y*xScale);
            g.fillOval(px,py, 5, 5);
        }
        
        g.setColor(Color.BLACK);
        g.drawString("Num. Boids: " + boids.size(), 10, 25);
        g.drawString("Framerate: " + framerate, 10, 40);
   }
}
