package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.objects.MarchingObject;

import javax.sound.sampled.Line;
import java.util.Arrays;

public class Plane extends MarchingObject {

    private char axis;

    public Plane(Material material, double[] position, double[] rotation, char axis) {
        super(material, position, rotation, new double[]{1.0d, 1.0d, 1.0d});

        this.axis = axis;
    }

    @Override
    public double getDistance(double[] p) {
        p = this.transformPoint(p);
        
        double d = Double.MAX_VALUE;

        if (this.axis == 'x') {
            d = Math.abs(p[0] - this.position[0]);
        }

        if (this.axis == 'y') {
            d = Math.abs(p[1] - this.position[1]);
        }

        if (this.axis == 'z') {
            d = Math.abs(p[2] - this.position[2]);
            
            double disp = this.material.getDisplacement(p, this._getUV(p));
            if (disp != 0.0d) {
                // System.out.println(disp);
                d -= disp * 0.1d;
                d *= Math.min(d, d * d * d);
                // System.out.println(d);
            }
        }

        return d;
    }

    @Override
    public double[] getNormal(double[] p) {
        p = this.transformPoint(p);

        double[] n = new double[] {1.0d, 0.0d, 0.0d};
        double[] bn = new double[] {0.0d, 1.0d, 0.0d};
        double[] t = new double[] {0.0d, 0.0d, 1.0d};

        if (this.axis == 'x') {
            if (p[0] < 0) {
                n = new double[]{1.0d, 0.0d, 0.0d};
                bn = new double[]{0.0d, 0.0d, -1.0d};
                t = new double[]{0.0d, 1.0d, 0.0d};
            } else {
                n = new double[]{-1.0d, 0.0d, 0.0d};
                bn = new double[]{0.0d, 0.0d, -1.0d};
                t = new double[]{0.0d, 1.0d, 0.0d};
            }
        } else if (this.axis == 'y') {
            if (p[1] < 0) {
                n = new double[]{0.0d, 1.0d, 0.0d};
                bn = new double[]{0.0d, 0.0d, 1.0d};
                t = new double[]{-1.0d, 0.0d, 0.0d};
            } else {
                n = new double[]{0.0d, -1.0d, 0.0d};
                bn = new double[]{0.0d, 0.0d, -1.0d};
                t = new double[]{1.0d, 0.0d, 0.0d};
            }
        } else if (this.axis == 'z') {
            if (p[2] < 0) {
                n = new double[]{0.0d, 0.0d, 1.0d};
                bn = new double[]{0.0d, -1.0d, 0.0d};
                t = new double[]{1.0d, 0.0d, 0.0d};
            } else {
                n = new double[]{0.0d, 0.0d, -1.0d};
                bn = new double[]{0.0d, 1.0d, 0.0d};
                t = new double[]{-1.0d, 0.0d, 0.0d};
            }
        }

        double[] nt = this.material.getNormal(p, this.getUV(p, n));
        // (T * N_ts.x) + (B * N_ts.y) + (N * N_ts.z)
        n = LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.add(LinearAlgebra.mul(t, nt[0]), LinearAlgebra.mul(bn, nt[1])), LinearAlgebra.mul(n, nt[2])), n);
        return LinearAlgebra.normalize(n);
    }

    @Override
    public double[] getUV(double[] p, double[] n) {
        return this._getUV(p);
    }

    private double[] _getUV(double[] p) {
        if (this.axis == 'z') {
            return new double[]{p[0], p[1], 0.0d};
        }
        if (this.axis == 'x') {
            return new double[]{p[1], p[2], 0.0d};
        }
        if (this.axis == 'y') {
            return new double[]{p[0], p[2], 0.0d};
        }
        return new double[3];
    }
}
