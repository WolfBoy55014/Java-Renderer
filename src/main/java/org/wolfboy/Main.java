package org.wolfboy;

import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.marching.MarchingCamera;
import org.wolfboy.renderer.marching.MarchingRenderer;
import org.wolfboy.renderer.marching.MarchingScene;
import org.wolfboy.renderer.marching.objects.Fractal;
import org.wolfboy.renderer.marching.objects.MarchingObject;
import org.wolfboy.renderer.marching.objects.primitive.Plane;
import org.wolfboy.renderer.marching.objects.primitive.Sphere;
import org.wolfboy.ui.UI;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        final int width = 1920;
        final int height = 1080;

        UI ui = new UI(width, height);
        MarchingCamera camera = new MarchingCamera(width, height, 1.2d);
        camera.setRotation(-0.4d, 0.0d, 0.0d);
        camera.setPosition(0.0d, -5.0d, 2.0d);

        MarchingObject[] objects = new MarchingObject[5];
        // objects[4] = new Plane(new Material(new Color(255, 255, 255)), new double[]{0.0d, 0.0d, -1.0d}, new double[]{0.0d, 0.0d, 0.0d}, 'z');
        // objects[3] = new Sphere(new Material(new Color(105, 234, 156)), new double[]{2.0d, 2.0d, 0.0d}, 1.0f);
        // objects[2] = new Sphere(new Material(new Color(186, 204, 109)), new double[]{-2.0d, 2.0d, 0.0d}, 1.0f);
        // objects[1] = new Sphere(new Material(new Color(200, 128, 228)), new double[]{2.0d, -2.0d, 0.0d}, 1.0f);
        // objects[0] = new Sphere(new Material(new Color(121, 216, 225)), new double[]{-2.0d, -2.0d, 0.0d}, 1.0f);
        objects[0] = new Fractal(new Material(new Color(121, 225, 194)), new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});

        MarchingScene scene = new MarchingScene(objects);

        MarchingRenderer renderer = new MarchingRenderer(scene, camera);

        Runnable[] tasks = new Runnable[width];

        int i = 0;
        while (i == 0) {
            i++;

            for (int x = 0; x < width; x++) {
                tasks[x] = new RenderTask(x, renderer, ui);
            }

            ExecutorService pool = Executors.newFixedThreadPool(8);
            long startTime = System.nanoTime();

            for (int x = 0; x < width; x++) {
                pool.execute(tasks[x]);
            }

            pool.shutdown();

            while (!pool.isTerminated()) {
                // Wait for all threads to finish
            }

            long endTime = System.nanoTime();

            long duration = (endTime - startTime);  // divide by 1000000 to get milliseconds.

            System.out.println("Time taken: " + duration / 1000000 + "ms");

            ui.display();

            // System.out.println(ui.getMousePos());
            double mouseX = Math.min(Math.max(ui.getMousePos().getX(), 0), width);
            double mouseY = Math.min(Math.max(ui.getMousePos().getY(), 0), height);
            double cameraYaw = ((mouseX / width) - 0.5d) * 1.2d;
            double cameraPitch = ((mouseY / height) - 0.5d) * 1.2d;
            camera.setRotation(cameraPitch, 0.0, cameraYaw);
        }
    }
}

class RenderTask implements Runnable {
    private final int x;
    private final MarchingRenderer renderer;
    private final UI ui;

    public RenderTask(int x, MarchingRenderer renderer, UI ui) {
        this.x = x;
        this.renderer = renderer;
        this.ui = ui;
    }

    public void run() {
        for (int y = 0; y < renderer.getCamera().getHeight(); y++) {
            Color color = renderer.renderPixel(x, y);
            ui.drawPixel(x, y, color);
            ui.display();
        }
    }
}