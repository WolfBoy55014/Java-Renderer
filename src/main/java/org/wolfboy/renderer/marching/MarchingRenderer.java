package org.wolfboy.renderer.marching;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Renderer;

import java.awt.*;

public class MarchingRenderer extends Renderer {

    private final double MAX_DISTANCE;
    private final double MIN_DISTANCE;
    private final int SPP;

    private final MarchingScene scene;

    public MarchingRenderer(MarchingScene scene, MarchingCamera camera) {
        super(scene, camera);
        this.scene = scene;

        MAX_DISTANCE = 100.0d;
        MIN_DISTANCE = 0.001d;
        SPP = 100;
    }

    private Ray march(Ray ray, double maxDistance) {
        double distance = 0.0d;
        while (distance < maxDistance) {
            double[] p = ray.getPosition();
            double d = this.scene.getDistance(p);

            ray.step(d);
            distance += d;

            if (d < this.MIN_DISTANCE) {
                return ray;
            }
        }
        return ray;
    }

    public Color renderPixel(int x, int y) {
        double[] color = {0.0d, 0.0d, 0.0d};

        for (int s = 0; s < SPP; s++) {
            Ray ray = this.camera.getRayAtPixel(x, y);

            ray = this.march(ray, MAX_DISTANCE);

            if (ray.getDistance() >= MAX_DISTANCE) {
                continue;
            }

            double[] p = ray.getPosition();

            Color objColor = this.scene.getNearestObject(ray.getPosition()).getMaterial().getColor();
            double[] light_pos = new double[]{0.0d, 0.0d, 5.0d};
            double[] light_dir = LinearAlgebra.normalize(LinearAlgebra.sub(light_pos, p));
            double[] n = this.scene.getNormal(p);

            double illumination = Math.min(LinearAlgebra.dot(n, light_dir), 1.0d);

            // Calculate shadows
            // float d = RayMarch(p+n*SURF_DIST*2., l);
            ray.setDirection(n);
            ray.step(this.MIN_DISTANCE * 2.0d);
            ray.setDirection(light_dir);
            ray.resetDistance();

            ray = this.march(ray, LinearAlgebra.distance(p, light_pos));
            if (ray.getDistance() < LinearAlgebra.distance(p, light_pos)) {
                illumination *= 0.2d;
            }

            illumination = Math.max(illumination, 0.2d);

            color[0] += objColor.getRed() * illumination;
            color[1] += objColor.getGreen() * illumination;
            color[2] += objColor.getBlue() * illumination;
        }
        color[0] /= SPP;
        color[1] /= SPP;
        color[2] /= SPP;

        // color = new Color((int) Math.max(this.camera.getDirectionAtPixel(x, y)[0] * 255, 0),
        //         0 /*(int) Math.max(this.camera.getDirectionAtPixel(x, y)[1] * 255, 0)*/,
        //         0 /*(int) Math.max(this.camera.getDirectionAtPixel(x, y)[2] * 255, 0)*/);

        // if (Math.abs(this.camera.getDirectionAtPixel(x, y)[1]) < 0.9d) {
        //     color = new Color(0, 255, 0);
        // }

        // if (Math.abs(this.camera.getDirectionAtPixel(x, y)[0]) < 0.007d) {
        //     color = new Color(255, 0, 0);
        // }

        // if (Math.abs(this.camera.getDirectionAtPixel(x, y)[2]) < 0.007d) {
        //     color = new Color(0, 0, 255);
        // }

        return new Color((int) color[0], (int) color[1], (int) color[2]);
    }
}
