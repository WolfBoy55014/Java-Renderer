package org.wolfboy.renderer.marching.lights;

import org.wolfboy.LinearAlgebra;

import java.awt.*;

public class DirectionalLight extends MarchingLight {

    public DirectionalLight(double[] rotation, double[] color, double intensity) {
        super(new double[]{0.0d, 0.0d, 0.0d}, rotation, color, intensity);
    }

    public DirectionalLight(double[] rotation, Color color, double intensity) {
        super(new double[]{0.0d, 0.0d, 0.0d}, rotation, color, intensity);
    }

    public double[] getLightDir(double[] p) {
        return LinearAlgebra.toLocal(new double[]{0.0d, 0.0d, 1.0d}, this.position, this.rotation, new double[]{1.0d, 1.0d, 1.0d});
    }
}
