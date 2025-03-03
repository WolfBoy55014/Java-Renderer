package org.wolfboy.renderer.marching;

import org.wolfboy.LinearAlgebra;

import java.awt.*;

public class Renderer {

    private Camera camera;
    private int width;
    private int height;

    private int MAX_STEPS;
    private double MAX_DISTANCE;
    private double MIN_DISTANCE;

    public Renderer(Camera camera) {
        this.camera = camera;
        this.width = camera.getWidth();
        this.height = camera.getHeight();

        this.MAX_STEPS = 100;
        this.MAX_DISTANCE = 100.0d;
        this.MIN_DISTANCE = 0.01d;
    }

    public Color renderPixel(int x, int y) {
        Color color = new Color(0, 0, 0);

        Ray ray = new Ray(this.camera.getDirectionAtPixel(x, y), this.camera.getPosition());

        // March along the ray
        double distance = 0.0d;
        while (distance < this.MAX_DISTANCE) {
            double[] p = ray.getPosition();
            double d = LinearAlgebra.distance(p, new double[]{0.0d, 0.0d, 0.0d}) - 1.0d;

            distance += d;
            ray.step(d);

            if (d < this.MIN_DISTANCE) {
                color = new Color((int) distance * 128, (int) distance * 128, (int) distance * 128);
                break;
            }
        }

        // color = new Color((int) Math.max(this.camera.getDirectionAtPixel(x, y)[0] * 255, 0), (int) Math.max(this.camera.getDirectionAtPixel(x, y)[1] * 255, 0), (int) Math.max(this.camera.getDirectionAtPixel(x, y)[2] * 255, 0));

        return color;
    }
}
