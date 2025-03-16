package org.wolfboy.renderer.marching.lights;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Light;

import java.awt.*;

public class MarchingLight extends Light {

    public MarchingLight(double[] position, double[] rotation, double[] color, double intensity) {
        super(position, rotation, color, intensity);
    }

    public MarchingLight(double[] position, double[] rotation, Color color, double intensity) {
        super(position, rotation, color, intensity);
    }

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

    public double[] transformPoint(double[] p) {
        return LinearAlgebra.toLocal(p, this.position, this.rotation, new double[]{1.0d, 1.0d, 1.0d});
    }
}
