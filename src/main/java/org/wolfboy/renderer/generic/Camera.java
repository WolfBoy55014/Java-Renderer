package org.wolfboy.renderer.generic;

import org.wolfboy.LinearAlgebra;

import java.util.Arrays;

public class Camera {

    protected double FOV;
    protected int width;
    protected int height;

    protected double[] position = new double[3];
    protected double[] rotation = new double[3];

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

    public void setPosition(double[] position) {
        this.position = position;
    }

    public void setRotation(double[] rotation) {
        this.rotation = rotation;
    }

    public double[] getRotation() {
        return rotation;
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

        direction[1] = 1.0d;

        if (y == (this.height / 2)) {
            direction[2] = 0.0d;
        } else {
            direction[2] = -(((y - (this.height / 2.0d)) / this.height) * FOV);
        }

        // System.out.println(Arrays.toString(direction));

        // Apply Camera rotation
        double[] roll = LinearAlgebra.rot(new double[]{direction[0], direction[2]}, this.rotation[1]);
        direction[0] = roll[0];
        direction[2] = roll[1];

        double[] yaw = LinearAlgebra.rot(new double[]{direction[0], direction[1]}, this.rotation[2]);
        direction[0] = yaw[0];
        direction[1] = yaw[1];

        double[] pitch = LinearAlgebra.rot(new double[]{direction[1], direction[2]}, -this.rotation[0]);
        direction[1] = pitch[0];
        direction[2] = pitch[1];

        // System.out.println(Arrays.toString(direction));

        return LinearAlgebra.normalize(direction);
    }

    public int[] directionToPixel(double[] direction) {
        int x = (int) ((direction[0] / FOV) * this.height + (this.width / 2.0d));
        int y = (int) ((-direction[2] / FOV) * this.height + (this.height / 2.0d));
        return new int[]{x, y};
    }
}
