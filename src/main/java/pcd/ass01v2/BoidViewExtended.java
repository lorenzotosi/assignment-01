package pcd.ass01v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BoidViewExtended {

    private BoidsModel model;
    private Button start;
    private Button stop;
    private TextField boidsInput;
    private JPanel northPanel;

    public BoidViewExtended(final BoidsModel model) {
        this.model = model;
        createUIComponents();
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    private void createUIComponents() {
        start = createButton("Start", (x -> {
            //model.setupThreads(Integer.parseInt(boidsInput.getText()));
            //model.getThreads().forEach(Thread::start);
        }));
        stop = createButton("Stop", null);
        boidsInput = createTextField();
        northPanel = createNorthPanel();
    }

    private Button createButton(String text, ActionListener e) {
        Button button = new Button(text);
        button.addActionListener(e);
        return button;
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel();
        panel.add(boidsInput);
        panel.add(start);
        panel.add(stop);
        return panel;
    }

    private TextField createTextField() {
        TextField field = new TextField();
        field.setPreferredSize(new Dimension(70, 30));
        return field;
    }

}
