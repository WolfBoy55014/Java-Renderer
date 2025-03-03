package org.wolfboy.renderer.marching;

import org.ojalgo.matrix.transformation.Rotation;
import org.wolfboy.LinearAlgebra;

import javax.sound.sampled.Line;
import java.util.Arrays;

public class Camera {
    private double FOV;
    private int width;
    private int height;

    private double[] position = new double[3];
    private double[] rotation = new double[3];
    private double[] direction = new double[3];

    public Camera(int width, int height, double FOV) {
        this.width = width;
        this.height = height;
        this.FOV = FOV;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /*public void setRotation(double[] rotation) {
        this.rotation = rotation;

        // Calculate the direction vector
        double x = Math.cos(rotation[0]) * Math.cos(rotation[1]);
        double y = Math.sin(rotation[0]) * Math.cos(rotation[1]);
        double z = Math.sin(rotation[1]);

        this.direction = new double[]{x, y, z};
    }*/

    public void setPosition(double[] position) {
        this.position = position;
    }

    public void setDirection(double[] direction) {
        this.direction = LinearAlgebra.normalize(direction);
    }

    public double[] getDirection() {
        return direction;
    }

    public double[] getPosition() {
        return position;
    }

    public double[] getDirectionAtPixel(int x, int y) {
        // (fragCoord-.5*iResolution.xy)/iResolution.y;

        double[] direction = new double[3];

        // Initial direction

        if (x == (this.width / 2)) {
            direction[0] = 0.0d;
        } else {
            direction[0] = ((x - (this.width / 2.0d)) / this.height) * FOV;
        }

        direction[1] = 0.0d;

        if (y == (this.height / 2)) {
            direction[2] = 0.0d;
        } else {
            direction[2] = ((y - (this.height / 2.0d)) / this.height) * FOV;
        }

        // Apply Camera rotation
        direction = LinearAlgebra.add(direction, this.direction);

        return LinearAlgebra.normalize(direction);
    }
}
