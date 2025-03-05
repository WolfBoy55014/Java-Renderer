package org.wolfboy.renderer.marching.objects;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;

public class Fractal extends MarchingObject {

    public Fractal(Material material, double[] position, double[] rotation, double[] scale) {
        super(material, position, rotation, scale);
    }

    public static double sdCross(double[] p) {
        double da = Math.max(Math.abs(p[0]), Math.abs(p[1]));
        double db = Math.max(Math.abs(p[1]), Math.abs(p[2]));
        double dc = Math.max(Math.abs(p[2]), Math.abs(p[0]));

        return Math.min(da, Math.min(db, dc)) - 1.0f;
    }

    public static double sdBox(double[] p, double[] b) {

        // Take the absolute value of each component of the point p, and subtract the box size b.
        double[] q = new double[]{0.0f, 0.0f, 0.0f};
        q[0] = (Math.abs(p[0]) - b[0]);
        q[1] = (Math.abs(p[1]) - b[1]);
        q[2] = (Math.abs(p[2]) - b[2]);

        // Find the maximum of each component of q, and clamp it to 0.
        double[] max = new double[]{0.0f, 0.0f, 0.0f};
        max[0] = Math.max(q[0], 0.0f);
        max[1] = Math.max(q[1], 0.0f);
        max[2] = Math.max(q[2], 0.0f);

        // Calculate the length of the vector (maxX, maxY, maxZ).
        double lengthOfMax = LinearAlgebra.magnitude(max);

        // Find the minimum of the maximum components of q, and clamp it to 0.
        double minOfMax = Math.min(Math.max(q[0], Math.max(q[1], q[2])), 0.0f);

        // Return the sum of the length and the minimum.
        return lengthOfMax + minOfMax;
    }


    public double getDistance(double[] p) {
        double[] b = {1.0f, 1.0f, 1.0f};
        double d = sdBox(p, b);
        double c = sdCross(LinearAlgebra.mul(p, 3.0d)) / 3.0d;
        return Math.max(d, -c);
    }
}
