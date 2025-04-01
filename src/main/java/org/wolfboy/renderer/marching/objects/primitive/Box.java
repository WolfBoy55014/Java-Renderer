package org.wolfboy.renderer.marching.objects.primitive;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.objects.MarchingObject;

import javax.sound.sampled.Line;

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

        if (p[0] >= this.sides[0]) {
            return new double[]{1.0d, 0.0d, 0.0d};
        }
        if (p[1] >= this.sides[1]) {
            return new double[]{0.0d, 1.0d, 0.0d};
        }
        if (p[2] >= this.sides[2]) {
            return new double[]{0.0d, 0.0d, 1.0d};
        }
        if (p[0] <= -this.sides[0]) {
            return new double[]{-1.0d, 0.0d, 0.0d};
        }
        if (p[1] <= -this.sides[1]) {
            return new double[]{0.0d, -1.0d, 0.0d};
        }
        if (p[2] <= -this.sides[2]) {
            return new double[]{0.0d, 0.0d, -1.0d};
        }
        return new double[]{0.0d, 0.0d, 1.0d};
    }
}
