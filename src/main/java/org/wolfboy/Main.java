package org.wolfboy;

import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.MarchingCamera;
import org.wolfboy.renderer.marching.MarchingRenderer;
import org.wolfboy.renderer.marching.MarchingScene;
import org.wolfboy.renderer.marching.objects.MarchingObject;
import org.wolfboy.renderer.marching.objects.primitive.Sphere;
import org.wolfboy.ui.UI;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        final int width = 640;
        final int height = 360;

        UI ui = new UI(width, height);
        MarchingCamera camera = new MarchingCamera(width, height, Math.PI);
        camera.setRotation(0.0d, 0.0d, 0.0d);
        camera.setPosition(0.0d, 0.0d, 0.0d);

        MarchingObject[] objects = new MarchingObject[4];
        objects[3] = new Sphere(new Material(new Color(105, 234, 156)), new double[]{2.0d, 2.0d, 0.0d}, 1.0f);
        objects[2] = new Sphere(new Material(new Color(186, 204, 109)), new double[]{-2.0d, 2.0d, 0.0d}, 1.0f);
        objects[1] = new Sphere(new Material(new Color(200, 128, 228)), new double[]{2.0d, -2.0d, 0.0d}, 1.0f);
        objects[0] = new Sphere(new Material(new Color(121, 216, 225)), new double[]{-2.0d, -2.0d, 0.0d}, 1.0f);
        // objects[0] = new Fractal(new Material(new Color(121, 225, 194)), new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});

        MarchingScene scene = new MarchingScene(objects);

        MarchingRenderer renderer = new MarchingRenderer(scene, camera);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = renderer.renderPixel(x, y);
                ui.drawPixel(x, y, color);
                ui.display();
            }
        }
    }
}