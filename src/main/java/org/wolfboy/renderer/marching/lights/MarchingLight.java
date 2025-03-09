package org.wolfboy.renderer.marching.lights;

import org.wolfboy.renderer.generic.Light;

public class MarchingLight extends Light {

    public MarchingLight(double[] position, double[] color) {
        super(position, color);
    }

    public double[] getLightDir(double[] p) {
        return new double[]{0.0d, 0.0d, 0.0d};
    }
}
