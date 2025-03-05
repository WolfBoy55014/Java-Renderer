package org.wolfboy.renderer.marching;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Renderer;

import java.awt.*;

public class MarchingRenderer extends Renderer {

    private int MAX_STEPS;
    private final double MAX_DISTANCE;
    private final double MIN_DISTANCE;

    private final MarchingScene scene;

    public MarchingRenderer(MarchingScene scene, MarchingCamera camera) {
        super(scene, camera);
        this.scene = scene;

        MAX_DISTANCE = 100.0d;
        MIN_DISTANCE = 0.001d;
    }

    public Color renderPixel(int x, int y) {
        Color color = new Color(0, 0, 0);

        Ray ray = new Ray(this.camera.getDirectionAtPixel(x, y), this.camera.getPosition());

        // March along the ray
        double distance = 0.0d;
        while (distance < this.MAX_DISTANCE) {
            double[] p = ray.getPosition();
            double d = this.scene.getDistance(p);

            double[] light_dir = new double[]{0.0d, 0.0d, 1.0d};
            double[] n = this.scene.getNormal(p);
            double illumination = Math.min(Math.max(0.2d, LinearAlgebra.dot(n, light_dir)), 1.0d);


            distance += d;
            ray.step(d);

            if (d < this.MIN_DISTANCE) {
                color = this.scene.getNearestObject(ray.getPosition()).getMaterial().getColor();
                color = new Color((int) (color.getRed() * illumination), (int) (color.getGreen() * illumination), (int) (color.getBlue() * illumination));
                break;
            }
        }

        // color = new Color((int) Math.max(this.camera.getDirectionAtPixel(x, y)[0] * 128 + 127, 0),
        //         (int) Math.max(this.camera.getDirectionAtPixel(x, y)[1] * 128 + 127, 0),
        //         (int) Math.max(this.camera.getDirectionAtPixel(x, y)[2] * 128 + 127, 0));

        return color;
    }
}
