package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.objects.MarchingObject;

import javax.sound.sampled.Line;
import java.util.Arrays;

public class Sphere extends MarchingObject {

    private double radius;

    public Sphere(Material material, double[] position, double[] rotation, double[] scale, double radius) {
        super(material, position, rotation, scale);
        this.radius = radius;
    }

    public Sphere(Material material, double[] position, double radius) {
        super(material, position, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});
        this.radius = radius;
    }

    @Override
    public double getDistance(double[] p) {
        p = this.transformPoint(p);

        return LinearAlgebra.magnitude(p) - this.radius;
    }

    @Override
    public double[] getNormal(double[] p) {
        p = this.transformPoint(p);

        return LinearAlgebra.normalize(p);
    }

    @Override
    public double[] getUV(double[] p, double[] n) {
        p = this.transformPoint(p);

        double x = 0.5 + (Math.atan2(p[1], p[0]) / (2 * Math.PI));
        double y = 0.5 + (Math.asin(p[2] / this.radius) / Math.PI);

        double diameter = this.radius * 2.0d;

        return new double[]{x % (1 / diameter) * diameter, y % (1 / diameter) * diameter};
    }
}
