package org.wolfboy.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderPanel extends JPanel {

    BufferedImage img;

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.drawImage(this.img, null, 0, 0);
    }

    public void updateImage(BufferedImage img) {
        this.img = img;
    }
}
