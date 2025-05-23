package org.wolfboy.renderer.marching;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.generic.Renderer;
import org.wolfboy.renderer.marching.lights.MarchingLight;
import org.wolfboy.renderer.marching.objects.MarchingObject;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MarchingRenderer extends Renderer {

    private final double MAX_DISTANCE;
    private final double MIN_DISTANCE;
    private BufferedImage skybox;
    private boolean useSkybox = false;

    private final MarchingScene scene;

    public MarchingRenderer(MarchingScene scene, MarchingCamera camera) {
        super(scene, camera);
        this.scene = scene;

        MAX_DISTANCE = 100.0d;
        MIN_DISTANCE = 0.0001d;
    }

    public void addSkybox(File image) {
        try {
            this.skybox = ImageIO.read(image);
            useSkybox = true;
        } catch (IOException e) {
            System.err.println("Error opening image: " + e.getMessage());
        }
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
        double[] bgColor = new double[]{0.0d, 0.0d, 0.0d};
        double[] color;

        Ray ray = this.camera.getRayAtPixel(x, y); // Must be sampled each sample for proper DoF blur
        color = this.renderWithRay(ray, bgColor, 0);

        color = LinearAlgebra.mul(color, 255.0d);

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

        // return new Color(((int) (color[0])), ((int) (color[1])), ((int) (color[2])));
        return new Color(Math.min((int) (color[0]), 255), Math.min((int) (color[1]), 255), Math.min((int) (color[2]), 255));
    }

    private double[] renderWithRay(Ray inputRay, double[] color, int bounce) {
        Ray ray = new Ray(inputRay.getDirection(), inputRay.getPosition());
        ray = this.march(ray, MAX_DISTANCE);

        if (ray.getDistance() >= MAX_DISTANCE) {
            if (useSkybox) {
                // Get skybox color
                // Convert to spherical coords
                double[] p = ray.getDirection();
                double x = 0.5 + (Math.atan2(p[1], p[0]) / (2 * Math.PI));
                double y = 0.5 + (Math.asin(p[2] / LinearAlgebra.magnitude(p)) / Math.PI);

                int skyColor = this.skybox.getRGB((int) (x * this.skybox.getWidth()), (int) (this.skybox.getHeight() - (y * this.skybox.getHeight())));
                return new double[]{((skyColor & 0x00ff0000) >> 16) / 255.0d, ((skyColor & 0x0000ff00) >> 8) / 255.0d, (skyColor & 0x000000ff) / 255.0d};
            }
            return color;
        }

        double[] p = ray.getPosition();
        MarchingObject nearestObject = this.scene.getNearestObject(p);
        double[] n = this.scene.getNormal(p);
        double[] uv = nearestObject.getUV(p, n);

        double metallic = this.scene.getNearestObject(p).getMaterial().getMetalic(p, uv);
        double specular = this.scene.getNearestObject(p).getMaterial().getSpecular(p, uv);
        double roughness = this.scene.getNearestObject(p).getMaterial().getRoughness(p, uv);

        double[] albedo = nearestObject.getMaterial().getAlbedo(p, uv);
        double[] diffuseColor = new double[3];
        double[] reflectedColor = new double[3];
        double[] specularColor = new double[3];
        double[] lightIntensity = new double[3];

        if ((specular > 0.0d) | (metallic > 0.0d)) {
            double[] r = new double[3];
            r[0] = n[0] + (((Math.random() * 2.0d) - 1.0d));
            r[1] = n[1] + (((Math.random() * 2.0d) - 1.0d));
            r[2] = n[2] + (((Math.random() * 2.0d) - 1.0d));
            n = LinearAlgebra.mix(n, r, roughness);
            n = LinearAlgebra.normalize(n);
        }

        // Calculate illumination per light
        for (MarchingLight light : this.scene.getLights()) {
            if (light == null) {
                continue;
            }

            double light_dist = Math.min(MAX_DISTANCE, light.getLightDistance(p));
            double[] light_dir = light.getLightDir(p);

            // Calculate shadows
            // float d = RayMarch(p+n*SURF_DIST*2., l);
            Ray shadowRay = new Ray(light_dir, LinearAlgebra.add(p, LinearAlgebra.mul(n, this.MIN_DISTANCE * 20.0d)));

            shadowRay = this.march(shadowRay, light_dist);
            if (shadowRay.getDistance() >= light_dist) {
                double diffuse = Math.max(LinearAlgebra.dot(n, light_dir), 0.0d);
                lightIntensity = LinearAlgebra.mul(light.getColor(), light.getIntensity(p));

                diffuseColor = LinearAlgebra.add(diffuseColor, LinearAlgebra.mul(LinearAlgebra.mul(lightIntensity, diffuse), LinearAlgebra.div(albedo, Math.PI)));
            }

            // Calculate specular reflections
            if (metallic > 0.0d | specular > 0.0d) {
                Ray specularRay = new Ray(light_dir, ray.getPosition());
                specularRay.reflect(n);
                specularColor = LinearAlgebra.mul(light.getColor(), Math.pow(Math.max(LinearAlgebra.dot(ray.getDirection(), specularRay.getDirection()), 0.0d), 800.0d));
            }
        }

        // Calculate reflections
        if (metallic > 0.0d) {
            double[] mul = LinearAlgebra.mul(n, this.MIN_DISTANCE * 4.0d);
            Ray reflectionRay = new Ray(ray.getDirection(), LinearAlgebra.add(ray.getPosition(), mul));
            reflectionRay.reflect(n);
            if (bounce < 16) {
                bounce++;
                reflectedColor = LinearAlgebra.mul(LinearAlgebra.add(this.renderWithRay(reflectionRay, color, bounce), specularColor), albedo);
            }
        }

        diffuseColor = LinearAlgebra.add(diffuseColor, specularColor);
        color = LinearAlgebra.mix(diffuseColor, reflectedColor, metallic);
        // color = LinearAlgebra.add(diffuseColor, LinearAlgebra.mul(specularColor, 10.0d));
        // color = LinearAlgebra.mul(specularColor, 10.0d);

        // color = LinearAlgebra.add(color, new double[]{uv[0], uv[1], 0.0d});
        // color = LinearAlgebra.add(LinearAlgebra.div(LinearAlgebra.add(n, 1.0d), 2.0d), color);
        // color = LinearAlgebra.abs(n);
        // color = LinearAlgebra.div(new double[]{ray.getSteps(), ray.getSteps(), ray.getSteps()}, 300.0d);
        // color = new double[]{bounce / 4.0d, bounce / 4.0d, bounce / 4.0d};
        return color;
    }
}
