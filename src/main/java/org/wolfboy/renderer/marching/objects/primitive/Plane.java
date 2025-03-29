package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.LinearAlgebra;
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
            return Math.abs(p[0] - this.position[0]);
        }

        if (this.axis == 'y') {
            return Math.abs(p[1] - this.position[1]);
        }

        if (this.axis == 'z') {
            return Math.abs(p[2] - this.position[2]);
        }

        return Double.MAX_VALUE;
    }

    @Override
    public double[] getNormal(double[] p) {
        p = this.transformPoint(p);

        if (this.axis == 'x') {
            if (p[0] < 0) {
                return new double[]{1.0d, 0.0d, 0.0d};
            } else {
                return new double[]{-1.0d, 0.0d, 0.0d};
            }
        }

        if (this.axis == 'y') {
            if (p[1] < 0) {
                return new double[]{0.0d, 1.0d, 0.0d};
            } else {
                return new double[]{0.0d, -1.0d, 0.0d};
            }
        }

        if (this.axis == 'z') {
            if (p[2] < 0) {
                return new double[]{0.0d, 0.0d, 1.0d};
            } else {
                return new double[]{0.0d, 0.0d, -1.0d};
            }
        }

        return new double[] {1.0d, 0.0d, 0.0d};
    }

    @Override
    public double[] getUV(double[] p) {
        if (this.axis == 'z') {
            return LinearAlgebra.mod(new double[]{p[0], p[1], 0.0d}, 1.0d);
        }
        if (this.axis == 'x') {
            return LinearAlgebra.mod(new double[]{p[1], p[2], 0.0d}, 1.0d);
        }
        if (this.axis == 'y') {
            return LinearAlgebra.mod(new double[]{p[0], p[2], 0.0d}, 1.0d);
        }
        return new double[3];
    }
}
