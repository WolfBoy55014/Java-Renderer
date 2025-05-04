package org.wolfboy.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UI {

    RenderPanel renderPanel;
    Container pane;
    ExtendedImage img;
    JFrame frame;

    public UI(int width, int height) {
        this.frame = new JFrame();

        this.pane = frame.getContentPane();
        this.pane.setLayout(new BorderLayout());

        this.img = new ExtendedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        this.renderPanel = new RenderPanel();
        this.renderPanel.updateImage(this.img);
        this.pane.add(this.renderPanel, BorderLayout.CENTER);

        this.frame.setVisible(true);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Insets insets = frame.getInsets();
        this.frame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
    }

    public ExtendedImage getImage() {
        return this.img;
    }

    public void drawPixel(int x, int y, Color color) {
        this.img.setColor(x, y, color);
    }

    public Color getPixel(int x, int y) {
        return this.img.getColor(x, y);
    }

    public void display() {
        this.renderPanel.updateImage(this.img);
        this.renderPanel.repaint();
    }

    public void clear() {
        Graphics2D g2d = this.img.createGraphics();
        g2d.setBackground(new java.awt.Color(0, 0, 0, 0)); // Transparent background
        g2d.clearRect(0, 0, this.img.getWidth(), this.img.getHeight());
        g2d.dispose(); // Release resources
    }

    public Point getMousePos() {
        Point screenPoint = MouseInfo.getPointerInfo().getLocation();
        Point componentPoint = new Point(screenPoint);
        SwingUtilities.convertPointFromScreen(componentPoint, this.pane);

        return componentPoint;
    }

    public int getWidth() {
        return this.img.getWidth();
    }

    public int getHeight() {
        return this.img.getHeight();
    }

    public void saveRender(String filename) {
        try {
            ImageIO.write(this.img, "png", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
