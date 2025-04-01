package org.wolfboy.renderer.marching.lights;

import org.wolfboy.LinearAlgebra;

import java.awt.*;

public class RectangleLight extends MarchingLight {

    private final double[] halfSize;
    private final double surfaceArea;

    public RectangleLight(double[] position, double[] rotation, Color color, double intensity, double[] size) {
        super(position, rotation, color, intensity);
        size = new double[]{size[0], size[1], 0.0d};
        this.halfSize = LinearAlgebra.div(size, 2.0d);
        this.surfaceArea = size[0] * size[1] * 2.0d;
    }

    public RectangleLight(double[] position, double[] rotation, double[] color, double intensity, double[] size) {
        super(position, rotation, color, intensity);
        size = new double[]{size[0], size[1], 0.0d};
        this.halfSize = LinearAlgebra.div(size, 2.0d);
        this.surfaceArea = size[0] * size[1] * 2.0d;
    }

    @Override
    public double[] getLightDir(double[] p) {
        p = this.transformPoint(p);

        // Pick random point in unit square
        double x = Math.random();
        double y = Math.random();

        x = (x - 0.5d) * 2.0d;
        y = (y - 0.5d) * 2.0d;
        double[] l = LinearAlgebra.mul(new double[]{x, y, 0.0d}, this.halfSize);
        // System.out.println(LinearAlgebra.magnitude(l));
        l = LinearAlgebra.toGlobal(l, this.position, this.rotation, new double[]{1.0d, 1.0d, 1.0d});
        double[] dir = LinearAlgebra.sub(p, l);
        dir = LinearAlgebra.normalize(LinearAlgebra.mul(dir, -1.0d));

        return dir;
    }

    @Override
    public double getIntensity(double[] p) {
        // We are going to pretend a disk light is just a collection of point lights shaped like a square
        // This means we can use the point light falloff equation

        double d = LinearAlgebra.distance(p, this.position);

        double falloff = (4 * Math.PI * Math.pow(d, 2)) * this.surfaceArea;

        return this.intensity / falloff;
    }
}
