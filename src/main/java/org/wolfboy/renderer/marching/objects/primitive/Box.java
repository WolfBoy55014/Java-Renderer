package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.objects.MarchingObject;

import javax.sound.sampled.Line;
import java.util.Arrays;

public class Box extends MarchingObject {

    private final double[] sides;

    public Box(Material material, double[] position, double[] rotation, double[] scale, double[] sides) {
        super(material, position, rotation, scale);
        this.sides = LinearAlgebra.div(sides, 2);
    }

    public Box(Material material, double[] position, double[] sides) {
        this(material, position, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d}, sides);
    }

    @Override
    public double getDistance(double[] p) {
        p = this.transformPoint(p);

//        a = np.divide(self.side_lengths, 2)
//        ray_position = ray.getPosition()
//
//        # Translate the point by the negative center to effectively move the box
//        p_relative = np.subtract(ray_position, self.pos)
//        d = np.abs(p_relative) - a  # Distance to each face along each axis
//        return np.max(d)  # Choose the maximum distance for the closest face

        double[] d = LinearAlgebra.sub(LinearAlgebra.abs(p), this.sides);
        return Math.max(d[0], Math.max(d[1], d[2]));
    }

    @Override
    public double[] getNormal(double[] p) {
        p = this.transformPoint(p);

        double[] n = new double[] {1.0d, 0.0d, 0.0d};
//        double[] bn = new double[] {0.0d, 1.0d, 0.0d};
//        double[] t = new double[] {0.0d, 0.0d, 1.0d};
//        double[] nt = this.material.getNormal(p, this._getUV(p));

        if (p[0] >= this.sides[0]) {
            n = new double[]{1.0d, 0.0d, 0.0d};
//            bn = new double[]{0.0d, 0.0d, 1.0d};
//            t = new double[]{0.0d, 1.0d, 0.0d};
        } else if (p[1] >= this.sides[1]) {
            n = new double[]{0.0d, 1.0d, 0.0d};
//            bn = new double[]{0.0d, 0.0d, 1.0d};
//            t = new double[]{1.0d, 0.0d, 0.0d};
        } else if (p[2] >= this.sides[2]) {
            n = new double[]{0.0d, 0.0d, 1.0d};
//            bn = new double[]{0.0d, 1.0d, 0.0d};
//            t = new double[]{1.0d, 0.0d, 0.0d};
        } else if (p[0] <= -this.sides[0]) {
            n = new double[]{-1.0d, 0.0d, 0.0d};
//            bn = new double[]{0.0d, 0.0d, 1.0d};
//            t = new double[]{0.0d, 1.0d, 0.0d};
        } else if (p[1] <= -this.sides[1]) {
            n = new double[]{0.0d, -1.0d, 0.0d};
//            bn = new double[]{0.0d, 0.0d, 1.0d};
//            t = new double[]{1.0d, 0.0d, 0.0d};
        } else if (p[2] <= -this.sides[2]) {
            n = new double[]{0.0d, 0.0d, -1.0d};
//            bn = new double[]{0.0d, 1.0d, 0.0d};
//            t = new double[]{1.0d, 0.0d, 0.0d};
        }

        // (T * N_ts.x) + (B * N_ts.y) + (N * N_ts.z)
//        n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2]));
        return LinearAlgebra.normalize(n);
    }

    @Override
    public double[] getUV(double[] p, double[] n) {
        p = this.transformPoint(p);

        double xXY = (p[0] + sides[0]) / 2.0d % (1 / (sides[0] * 2.0d)) * (sides[0] * 2.0d);
        double yXY = (p[1] + sides[1]) / 2.0d % (1 / (sides[1] * 2.0d)) * (sides[1] * 2.0d);
        double xYZ = (p[1] + sides[1]) / 2.0d % (1 / (sides[1] * 2.0d)) * (sides[1] * 2.0d);
        double yYZ = (p[2] + sides[2]) / 2.0d % (1 / (sides[2] * 2.0d)) * (sides[2] * 2.0d);
        double xXZ = (p[0] + sides[0]) / 2.0d % (1 / (sides[0] * 2.0d)) * (sides[0] * 2.0d);
        double yXZ = (p[2] + sides[2]) / 2.0d % (1 / (sides[2] * 2.0d)) * (sides[2] * 2.0d);

        n = LinearAlgebra.abs(n);

        return new double[]{xXY * n[2] + xXZ * n[1] + xYZ * n[0], yXY * n[2] + yXZ * n[1] + yYZ * n[0]};
    }
}
