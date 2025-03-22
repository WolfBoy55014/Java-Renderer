package org.wolfboy.renderer.marching.lights;

import org.wolfboy.LinearAlgebra;

import java.awt.*;

public class DiskLight extends MarchingLight {

    private final double radius;
    private final double surfaceArea;

    public DiskLight(double[] position, double[] rotation, Color color, double intensity, double radius) {
        super(position, rotation, color, intensity);
        this.radius = radius;
        this.surfaceArea = 2.0d * Math.PI * Math.pow(radius, 2);
    }

    public DiskLight(double[] position, double[] rotation, double[] color, double intensity, double radius) {
        super(position, rotation, color, intensity);
        this.radius = radius;
        this.surfaceArea = 2.0d * Math.PI * Math.pow(radius, 2);
    }

    @Override
    public double[] getLightDir(double[] p) {
        p = this.transformPoint(p);

        // Pick random point in disk
        double x = Math.random();
        double y = Math.random();

        while (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) > this.radius) {
            x = Math.random();
            y = Math.random();
        }

        x = (x - 0.5d) * 2.0d;
        y = (y - 0.5d) * 2.0d;
        double[] l = LinearAlgebra.mul(new double[]{x, y, 0.0d}, this.radius);
        // System.out.println(LinearAlgebra.magnitude(l));
        l = LinearAlgebra.toGlobal(l, this.position, this.rotation, new double[]{1.0d, 1.0d, 1.0d});
        double[] dir = LinearAlgebra.sub(p, l);
        dir = LinearAlgebra.normalize(LinearAlgebra.mul(dir, -1.0d));

        return dir;
    }

    @Override
    public double getIntensity(double[] p) {
        // We are going to pretend a disk light is just a collection of point lights shaped like a disk
        // This means we can use the point light falloff equation

        double d = LinearAlgebra.distance(p, this.position);

        // System.out.println(d);

        double falloff = (4 * Math.PI * Math.pow(d, 2)) * this.surfaceArea;

        // System.out.println(this.intensity / falloff);

        return this.intensity / falloff;
    }
}
