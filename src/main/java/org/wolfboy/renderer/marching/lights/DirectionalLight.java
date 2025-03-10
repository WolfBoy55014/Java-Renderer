package org.wolfboy.renderer.marching.lights;

import org.wolfboy.LinearAlgebra;

import java.awt.*;

public class DirectionalLight extends MarchingLight {

    public DirectionalLight(double[] direction, double[] color, double intensity) {
        super(direction, color, intensity);
        this.position = LinearAlgebra.normalize(this.position);
    }

    public DirectionalLight(double[] direction, Color color, double intensity) {
        super(direction, color, intensity);
        this.position = LinearAlgebra.normalize(this.position);
    }

    public double[] getLightDir(double[] p) {
        return this.position;
    }
}
