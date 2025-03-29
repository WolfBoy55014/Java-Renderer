package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.objects.MarchingObject;

public class Torus extends MarchingObject {

    private double radiusMinor;
    private double radiusMajor;

    public Torus(Material material, double[] position, double[] rotation, double[] scale, double radiusMinor, double radiusMajor) {
        super(material, position, rotation, scale);
        this.radiusMinor = radiusMinor;
        this.radiusMajor = radiusMajor;
    }

    public Torus(Material material, double[] position, double radiusMinor, double radiusMajor) {
        super(material, position, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});
        this.radiusMinor = radiusMinor;
        this.radiusMajor = radiusMajor;
    }

    @Override
    public double getDistance(double[] p) {
        p = this.transformPoint(p);

        double[] q = new double[]{LinearAlgebra.magnitude(new double[]{p[0], p[1], 0.0d}) - this.radiusMajor, p[2], 0.0d};
        return LinearAlgebra.magnitude(q) - radiusMinor;
    }

    @Override
    public double[] getUV(double[] p) {
        p = this.transformPoint(p);

        double x = 0.5 + (Math.atan2(p[1], p[0]) / (2 * Math.PI));
        double y = 0.5 + (Math.asin(p[2] / this.radiusMajor) / Math.PI);

        double diameter = this.radiusMajor * 2.0d;

        return new double[]{x % (1 / diameter) * diameter, y % (1 / diameter) * diameter};
    }
}
