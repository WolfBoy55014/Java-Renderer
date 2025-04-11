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
        double[] n = LinearAlgebra.normalize(p);
        double[] nt = this.material.getNormal(p, this._getUV(p));

        if (LinearAlgebra.accumulate(nt) == 0.0d) {
            return n;
        }

        double[] q = LinearAlgebra.sphericalToCartesian(LinearAlgebra.add(LinearAlgebra.cartesianToSpherical(p), new double[]{0.001d, 0.0d, 0.0d}));
        double[] bn = LinearAlgebra.normalize(LinearAlgebra.sub(q, p));
        double[] t = LinearAlgebra.mul(LinearAlgebra.cross(bn, q), -1.0d);

        n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2])), n);

        return LinearAlgebra.normalize(n);
    }

    @Override
    public double[] getUV(double[] p, double[] n) {
        p = this.transformPoint(p);
        return this._getUV(p);
    }

    private double[] _getUV(double[] p) {
        double x = 0.5 + (Math.atan2(p[1], p[0]) / (2 * Math.PI));
        double y = 0.5 + (Math.asin(p[2] / this.radius) / Math.PI);

        double diameter = this.radius * 2.0d;

        return new double[]{x % (1 / diameter) * diameter, y % (1 / diameter) * diameter};
    }
}
