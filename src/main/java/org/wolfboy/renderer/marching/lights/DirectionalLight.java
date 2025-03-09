package org.wolfboy.renderer.marching.lights;

public class DirectionalLight extends MarchingLight {

    public DirectionalLight(double[] direction, double[] color) {
        super(direction, color);
    }

    public double[] getLightDir(double[] p) {
        return this.position;
    }
}
