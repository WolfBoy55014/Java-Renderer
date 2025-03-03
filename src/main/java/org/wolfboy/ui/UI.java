package org.wolfboy.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class UI {

    RenderPanel renderPanel;
    Container pane;
    BufferedImage img;
    JFrame frame;

    public UI(int width, int height) {
        this.frame = new JFrame();

        this.pane = frame.getContentPane();
        this.pane.setLayout(new BorderLayout());

        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        this.renderPanel = new RenderPanel();
        this.renderPanel.updateImage(this.img);
        this.pane.add(this.renderPanel, BorderLayout.CENTER);

        this.frame.setVisible(true);
        Insets insets = frame.getInsets();
        this.frame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void drawPixel(int x, int y, Color color) {
        img.setRGB(x, y, color.getRGB());
    }

    public void display() {
        this.renderPanel.updateImage(this.img);
        this.renderPanel.repaint();
    }


}
