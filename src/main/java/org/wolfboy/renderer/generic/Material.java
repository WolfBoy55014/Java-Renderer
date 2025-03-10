package org.wolfboy.renderer.generic;

import java.awt.*;
import java.util.Arrays;

public class Material {

    protected double[] albedo; // Surface "color" in [0, 1]
    protected float reflectivity;
    protected float transparency;
    protected float refractiveIndex;

    public Material(double[] color, float reflectivity, float transparency, float refractiveIndex) {
        this.albedo = color;
        this.reflectivity = reflectivity;
        this.transparency = transparency;
        this.refractiveIndex = refractiveIndex;
    }

    public Material(Color color, float reflectivity, float transparency, float refractiveIndex) {
        this(new double[]{color.getRed() / 255.0d, color.getGreen() / 255.0d, color.getBlue() / 255.0d}, reflectivity, transparency, refractiveIndex);
    }

    public Material(double[] color) {
        this(color, 0, 0, 0);
    }

    public Material(Color color) {
        this(color, 0, 0, 0);
    }

    public double[] getColor() {
        return this.albedo;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getTransparency() {
        return transparency;
    }

    public float getRefractiveIndex() {
        return refractiveIndex;
    }
}
