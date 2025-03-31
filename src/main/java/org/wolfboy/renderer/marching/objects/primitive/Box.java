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
        double[] bn = new double[] {0.0d, 1.0d, 0.0d};
        double[] t = new double[] {0.0d, 0.0d, 1.0d};
        double[] nt = this.material.getNormal(p, this._getUV(p));

        if (p[0] >= this.sides[0]) {
            n = new double[]{1.0d, 0.0d, 0.0d};
            bn = new double[]{0.0d, 0.0d, 1.0d};
            t = new double[]{0.0d, 1.0d, 0.0d};
            n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2]));
        } else if (p[1] >= this.sides[1]) {
            n = new double[]{0.0d, 1.0d, 0.0d};
            bn = new double[]{0.0d, 0.0d, 1.0d};
            t = new double[]{1.0d, 0.0d, 0.0d};
            n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2]));
        } else if (p[2] >= this.sides[2]) {
            n = new double[]{0.0d, 0.0d, 1.0d};
            bn = new double[]{0.0d, 1.0d, 0.0d};
            t = new double[]{1.0d, 0.0d, 0.0d};
            n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2]));
        } else if (p[0] <= -this.sides[0]) {
            n = new double[]{-1.0d, 0.0d, 0.0d};
            bn = new double[]{0.0d, 0.0d, 1.0d};
            t = new double[]{0.0d, 1.0d, 0.0d};
            n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2]));
        } else if (p[1] <= -this.sides[1]) {
            n = new double[]{0.0d, -1.0d, 0.0d};
            bn = new double[]{0.0d, 0.0d, 1.0d};
            t = new double[]{1.0d, 0.0d, 0.0d};
            n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2]));
        } else if (p[2] <= -this.sides[2]) {
            n = new double[]{0.0d, 0.0d, -1.0d};
            bn = new double[]{0.0d, 1.0d, 0.0d};
            t = new double[]{1.0d, 0.0d, 0.0d};
            n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2]));
        }

        // (T * N_ts.x) + (B * N_ts.y) + (N * N_ts.z)
        // TODO: This is in the wrong spot, it needs to be in every if block.
        return LinearAlgebra.normalize(n);
    }

    @Override
    public double[] getUV(double[] p) {
        p = this.transformPoint(p);
        return this._getUV(p);
    }

    private double[] _getUV(double[] p) {
        double[] f = LinearAlgebra.abs(p);
        double x = 0.0d;
        double y = 0.0d;

        if (f[2] >= f[1] && f[2] >= f[0]) {
            x = ((p[0] + sides[0]) / 2.0d) % (1 / (sides[0] * 2.0d)) * (sides[0] * 2.0d);
            y = (p[1] + sides[1]) / 2.0d % (1 / (sides[1] * 2.0d)) * (sides[1] * 2.0d);
        } else if (f[0] >= f[1] && f[0] >= f[2]) {
            x = (p[1] + sides[1]) / 2.0d % (1 / (sides[1] * 2.0d)) * (sides[1] * 2.0d);
            y = (p[2] + sides[2]) / 2.0d % (1 / (sides[2] * 2.0d)) * (sides[2] * 2.0d);
        } else {
            x = (p[0] + sides[0]) / 2.0d % (1 / (sides[0] * 2.0d)) * (sides[0] * 2.0d);
            y = (p[2] + sides[2]) / 2.0d % (1 / (sides[2] * 2.0d)) * (sides[2] * 2.0d);
        }



        return new double[]{x, y};
    }
}
