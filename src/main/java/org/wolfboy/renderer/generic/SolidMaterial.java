package org.wolfboy.renderer.generic;

import java.awt.*;

public class SolidMaterial implements Material {

    private double[] albedo;
    private double roughness;
    private double specular;
    private double metalic;
    private double transmission;

    public SolidMaterial(double[] albedo) {
        this.albedo = albedo;
    }

    public SolidMaterial(Color albedo) {
        this(new double[]{albedo.getRed() / 255.0d, albedo.getGreen() / 255.0d, albedo.getBlue() / 255.0d});
    }

    @Override
    public double[] getAlbedo(double[] p, double[] uv) {
        return this.albedo;
    }

    @Override
    public double[] getNormal(double[] p, double[] uv) {
        return new double[]{0.0d, 0.0d, 0.0d};
    }

    @Override
    public float getRoughness(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getSpecular(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getMetalic(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getTransmission(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getEmission(double[] p, double[] uv) {
        return 0;
    }
}
