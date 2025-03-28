package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.objects.MarchingObject;

public class Sphere extends MarchingObject {

    private double radius;

    public Sphere(Material material, double[] position, double[] rotation, double[] scale) {
        super(material, position, rotation, scale);
    }

    public Sphere(Material material, double[] position, float radius) {
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
}
