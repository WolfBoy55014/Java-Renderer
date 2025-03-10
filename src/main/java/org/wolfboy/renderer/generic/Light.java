package org.wolfboy.renderer.generic;

import java.awt.*;

public class Light {
    protected double[] position;
    protected double intensity;
    protected double[] color;

    public Light(double[] position, double[] color, double intensity) {
        this.position = position;
        this.intensity = intensity;
        this.color = color;
    }

    public Light(double[] position, Color color, double intensity) {
        this(position, new double[]{color.getRed() / 255.0d, color.getGreen() / 255.0d, color.getBlue() / 255.0d}, intensity);
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

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}
