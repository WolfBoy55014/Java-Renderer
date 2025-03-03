package org.wolfboy;

import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.generic.Object;
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
        MarchingCamera camera = new MarchingCamera(width, height, 1.2d);
        camera.setDirection(new double[]{0.0d, 1.0d, -0.5d});
        camera.setPosition(new double[]{0.0d, -2.0d, 2.0d});

        MarchingObject[] objects = new MarchingObject[1];
        objects[0] = new Sphere(new Material(new Color(121, 225, 194)), new double[]{0.0d, 0.0d, 0.0d}, 1.0f);

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