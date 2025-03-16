package org.wolfboy.renderer.marching.lights;

import org.wolfboy.LinearAlgebra;

import java.awt.*;
import java.util.Arrays;

public class DiskLight extends MarchingLight {

    private double radius;

    public DiskLight(double[] position, double[] rotation, Color color, double intensity, double radius) {
        super(position, rotation, color, intensity);
        this.radius = radius;
    }

    public DiskLight(double[] position, double[] rotation, double[] color, double intensity, double radius) {
        super(position, rotation, color, intensity);
        this.radius = radius;
    }

    public double[] getLightDir(double[] p) {
        p = this.transformPoint(p);

        // Pick random point in disk
        double x = Math.random();
        double y = Math.random();
        x = (x - 0.5d) * 2.0d;
        y = (y - 0.5d) * 2.0d;
        double[] l = LinearAlgebra.mul(LinearAlgebra.normalize(new double[]{x, y, 0.0d}), this.radius);
        l = LinearAlgebra.toGlobal(l, this.position, this.rotation, new double[]{1.0d, 1.0d, 1.0d});
        double[] dir = LinearAlgebra.sub(p, l);
        dir = LinearAlgebra.normalize(LinearAlgebra.mul(dir, -1.0d));

        return dir;
    }
}
