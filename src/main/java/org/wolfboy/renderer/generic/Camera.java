package org.wolfboy.renderer.generic;

import org.wolfboy.LinearAlgebra;

public class Camera {

    protected double FOV;
    protected int width;
    protected int height;

    protected double[] position = new double[3];
    protected double[] direction = new double[3];

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
            direction[2] = -(((y - (this.height / 2.0d)) / this.height) * FOV);
        }

        // Apply Camera rotation
        direction = LinearAlgebra.add(direction, this.direction);

        return LinearAlgebra.normalize(direction);
    }

    public int[] directionToPixel(double[] direction) {
        int x = (int) ((direction[0] / FOV) * this.height + (this.width / 2.0d));
        int y = (int) ((-direction[2] / FOV) * this.height + (this.height / 2.0d));
        return new int[]{x, y};
    }
}
