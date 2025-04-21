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

    public SolidMaterial(double[] albedo, double metallic) {
        this.albedo = albedo;
        this.metalic = metallic;
    }

    public SolidMaterial(double[] albedo, double metallic, double roughness) {
        this.albedo = albedo;
        this.metalic = metallic;
        this.roughness = roughness;
    }

    public SolidMaterial(double[] albedo, double metallic, double roughness, double specular) {
        this.albedo = albedo;
        this.metalic = metallic;
        this.roughness = roughness;
        this.specular = specular;
    }

    public SolidMaterial(Color albedo) {
        this(new double[]{albedo.getRed() / 255.0d, albedo.getGreen() / 255.0d, albedo.getBlue() / 255.0d});
    }

    public SolidMaterial(Color albedo, double metalic) {
        this(new double[]{albedo.getRed() / 255.0d, albedo.getGreen() / 255.0d, albedo.getBlue() / 255.0d}, metalic);
    }

    public SolidMaterial(Color albedo, double metalic, double roughness) {
        this(new double[]{albedo.getRed() / 255.0d, albedo.getGreen() / 255.0d, albedo.getBlue() / 255.0d}, metalic, roughness);
    }

    public SolidMaterial(Color albedo, double metalic, double roughness, double specular) {
        this(new double[]{albedo.getRed() / 255.0d, albedo.getGreen() / 255.0d, albedo.getBlue() / 255.0d}, metalic, roughness, specular);
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
    public double getDisplacement(double[] p, double[] uv) {
        return 0.0d;
    }

    @Override
    public double getRoughness(double[] p, double[] uv) {
        return this.roughness;
    }

    @Override
    public double getSpecular(double[] p, double[] uv) {
        return this.specular;
    }

    @Override
    public double getMetalic(double[] p, double[] uv) {
        return this.metalic;
    }

    @Override
    public double getTransmission(double[] p, double[] uv) {
        return 0.0d;
    }

    @Override
    public double getEmission(double[] p, double[] uv) {
        return 0.0d;
    }
}
