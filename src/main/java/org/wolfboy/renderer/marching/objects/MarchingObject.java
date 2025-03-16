package org.wolfboy.renderer.marching.objects;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.generic.Object;

public class MarchingObject extends Object {

    public MarchingObject(Material material, double[] position, double[] rotation, double[] scale) {
        super(material, position, rotation, scale);
    }

    public double getDistance(double[] p) {
        return 0.0d;
    }

    public double[] getNormal(double[] p) {
        /*
        vec3 GetNormal(vec3 p) {
	        float d = GetDist(p);
            vec2 e = vec2(.01, 0);

            vec3 n = d - vec3(
                GetDist(p-e.xyy),
                GetDist(p-e.yxy),
                GetDist(p-e.yyx));

            return normalize(n);
        }
         */

        double d = this.getDistance(p);
        double[] e = new double[]{0.001d, 0.0d, 0.0d};

        double[] n = LinearAlgebra.sub(new double[]{d, d, d}, new double[]{
            this.getDistance(LinearAlgebra.sub(p, new double[]{e[0], e[1], e[2]})),
            this.getDistance(LinearAlgebra.sub(p, new double[]{e[1], e[0], e[2]})),
            this.getDistance(LinearAlgebra.sub(p, new double[]{e[1], e[2], e[0]}))
        });

        return LinearAlgebra.normalize(n);
    }

    public double[] transformPoint(double[] p) {
        return LinearAlgebra.toLocal(p, this.position, this.rotation, this.scale);
    }
}
