package org.wolfboy.renderer.marching.lights;

import org.wolfboy.LinearAlgebra;

import java.awt.*;

public class PointLight extends MarchingLight {

    public PointLight(double[] position, double[] color, double intensity) {
        super(position, color, intensity);
    }

    public PointLight(double[] position, Color color, double intensity) {
        super(position, color, intensity);
    }

    @Override
    public double[] getLightDir(double[] p) {
        return LinearAlgebra.normalize(LinearAlgebra.sub(this.position, p));
    }

    @Override
    public double getLightDistance(double[] p) {
        return LinearAlgebra.distance(p, this.position);
    }

    @Override
    public double getIntensity(double[] p) {
        double d = LinearAlgebra.distance(p, this.position);
        double falloff = 4.0d * Math.PI * (d * d);
        return this.intensity / falloff;
    }
}
