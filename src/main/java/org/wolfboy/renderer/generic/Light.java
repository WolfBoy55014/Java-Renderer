package org.wolfboy.renderer.generic;

import org.wolfboy.LinearAlgebra;

import java.awt.*;

public class Light {
    protected double[] position;
    protected double[] rotation;
    protected double intensity;
    protected double[] color;

    public Light(double[] position, double[] rotation, double[] color, double intensity) {
        this.rotation = rotation;
        this.position = position;
        this.intensity = intensity;
        this.color = LinearAlgebra.normalize(color);
    }

    public Light(double[] position, double[] rotation, Color color, double intensity) {
        this(position, rotation, new double[]{color.getRed() / 255.0d, color.getGreen() / 255.0d, color.getBlue() / 255.0d}, intensity);
    }

    public Light(double[] position, double[] color, double intensity) {
        this(position, new double[]{0.0d, 0.0d, 0.0d}, color, intensity);
    }

    public Light(double[] position, Color color, double intensity) {
        this(position, new double[]{0.0d, 0.0d, 0.0d}, color, intensity);
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

    public double getIntensity(double[] p) {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}
