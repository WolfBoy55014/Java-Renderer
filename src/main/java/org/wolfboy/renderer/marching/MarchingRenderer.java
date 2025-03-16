package org.wolfboy.renderer.marching;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Renderer;
import org.wolfboy.renderer.marching.lights.MarchingLight;

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
            double[] n = this.scene.getNormal(p);
            double[] albedo = LinearAlgebra.div(this.scene.getNearestObject(ray.getPosition()).getMaterial().getColor(), Math.PI);
            double[] illumination = new double[]{0.0d, 0.0d, 0.0d};

            // Calculate illumination per light
            for (MarchingLight light : this.scene.getLights()) {
                if (light == null) {
                    continue;
                }

                double light_dist = Math.min(MAX_DISTANCE, light.getLightDistance(p));
                double[] light_dir = light.getLightDir(p);

                // Calculate shadows
                // float d = RayMarch(p+n*SURF_DIST*2., l);
                Ray shadowRay = new Ray(light_dir, LinearAlgebra.add(p, LinearAlgebra.mul(n, this.MIN_DISTANCE * 2.0d)));

                shadowRay = this.march(shadowRay, light_dist);
                if (shadowRay.getDistance() >= light_dist) {
                    illumination[0] += Math.max(LinearAlgebra.dot(n, light_dir), 0.0d) * light.getIntensity() * light.getColor()[0];
                    illumination[1] += Math.max(LinearAlgebra.dot(n, light_dir), 0.0d) * light.getIntensity() * light.getColor()[1];
                    illumination[2] += Math.max(LinearAlgebra.dot(n, light_dir), 0.0d) * light.getIntensity() * light.getColor()[2];
                }
            }

            color[0] += albedo[0] * illumination[0];
            color[1] += albedo[1] * illumination[1];
            color[2] += albedo[2] * illumination[2];
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

        return new Color((int) (color[0]), (int) (color[1]), (int) (color[2]));
    }
}
