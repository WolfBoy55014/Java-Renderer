package org.wolfboy.renderer.marching.lights;

import org.wolfboy.renderer.generic.Light;

import java.awt.*;

public class MarchingLight extends Light {

    public MarchingLight(double[] position, double[] color, double intensity) {
        super(position, color, intensity);
    }

    public MarchingLight(double[] position, Color color, double intensity) {
        super(position, color, intensity);
    }

    public double[] getLightDir(double[] p) {
        return new double[]{0.0d, 0.0d, 0.0d};
    }

    public double getLightDistance(double[] p) {
        return Double.MAX_VALUE;
    }
}
