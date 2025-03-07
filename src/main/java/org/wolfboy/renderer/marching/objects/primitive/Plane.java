package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.objects.MarchingObject;

public class Plane extends MarchingObject {

    private char axis;

    public Plane(Material material, double[] position, double[] rotation, char axis) {
        super(material, position, rotation, new double[]{1.0d, 1.0d, 1.0d});

        this.axis = axis;
    }

    @Override
    public double getDistance(double[] p) {
        p = this.transformPoint(p);

        if (this.axis == 'x') {
            return p[0] - this.position[0];
        }

        if (this.axis == 'y') {
            return p[1] - this.position[1];
        }

        if (this.axis == 'z') {
            return p[2] - this.position[2];
        }

        return Double.MAX_VALUE;
    }

    @Override
    public double[] getNormal(double[] p) {
        p = this.transformPoint(p);

        if (this.axis == 'x') {
            return new double[] {1.0d, 0.0d, 0.0d};
        }

        if (this.axis == 'y') {
            return new double[] {0.0d, 1.0d, 0.0d};
        }

        if (this.axis == 'z') {
            return new double[] {0.0d, 0.0d, 1.0d};
        }

        return new double[] {1.0d, 0.0d, 0.0d};
    }
}
