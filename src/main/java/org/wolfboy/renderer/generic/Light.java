package org.wolfboy.renderer.generic;

public class Light {
    protected double[] position;
    protected double[] direction;
    protected double[] color;

    public Light(double[] position, double[] direction, double[] color) {
        this.position = position;
        this.direction = direction;
        this.color = color;
    }

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

    public double[] getDirection() {
        return direction;
    }

    public void setDirection(double[] direction) {
        this.direction = direction;
    }

    public double[] getColor() {
        return color;
    }
}
