package org.wolfboy.renderer.generic;

public class Light {
    protected double[] position;
    protected double[] color;

    public Light(double[] position, double[] color) {
        this.position = position;
        this.color = color;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getColor() {
        return color;
    }
}
