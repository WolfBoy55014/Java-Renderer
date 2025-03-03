package org.wolfboy;

import org.wolfboy.renderer.marching.Camera;
import org.wolfboy.renderer.marching.Renderer;
import org.wolfboy.ui.UI;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        final int width = 640;
        final int height = 360;

        UI ui = new UI(width, height);
        Camera camera = new Camera(width, height, 1.2d);

        camera.setDirection(new double[]{0.0d, 1.0d, 0.0d});
        camera.setPosition(new double[]{0.0d, -2.0d, 0.0d});

        Renderer renderer = new Renderer(camera);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = renderer.renderPixel(x, y);
                ui.drawPixel(x, y, color.getRed(), color.getGreen(), color.getBlue());
                ui.display();
            }
        }
    }
}