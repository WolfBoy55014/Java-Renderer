package org.wolfboy.renderer.generic;

import java.awt.*;

public class Material {

    protected Color color;
    protected float reflectivity;
    protected float transparency;
    protected float refractiveIndex;

    public Material(Color color, float reflectivity, float transparency, float refractiveIndex) {
        this.color = color;
        this.reflectivity = reflectivity;
        this.transparency = transparency;
        this.refractiveIndex = refractiveIndex;
    }

    public Material(Color color) {
        this(color, 0, 0, 0);
    }

    public Color getColor() {
        return this.color;
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
