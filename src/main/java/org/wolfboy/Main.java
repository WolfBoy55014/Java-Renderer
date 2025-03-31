package org.wolfboy;

import org.wolfboy.renderer.generic.Material;
import org.wolfboy.renderer.generic.SolidMaterial;
import org.wolfboy.renderer.generic.TextureMaterial;
import org.wolfboy.renderer.marching.MarchingCamera;
import org.wolfboy.renderer.marching.MarchingRenderer;
import org.wolfboy.renderer.marching.MarchingScene;
import org.wolfboy.renderer.marching.lights.DirectionalLight;
import org.wolfboy.renderer.marching.lights.DiskLight;
import org.wolfboy.renderer.marching.lights.MarchingLight;
import org.wolfboy.renderer.marching.lights.SquareLight;
import org.wolfboy.renderer.marching.objects.MarchingObject;
import org.wolfboy.renderer.marching.objects.primitive.Box;
import org.wolfboy.renderer.marching.objects.primitive.Plane;
import org.wolfboy.renderer.marching.objects.primitive.Sphere;
import org.wolfboy.renderer.marching.objects.primitive.Torus;
import org.wolfboy.ui.UI;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        // 8k (7680 * 4320)
        // 4k (3840 * 2160)
        // HD (1920 * 1080)
        // 720p (1280 * 720)
        // 480p (854 * 480)
        // 360p (640 * 360)

        final int width = 1920;
        final int height = 1080;
        final boolean save = true;
        final int SPP = 512;

        UI ui = new UI(width, height);
        MarchingCamera camera = new MarchingCamera(width, height, 1.2d, 6.75, 0.0d);
        camera.setRotation(-0.5d, 0.0d, 0.2d);
        camera.setPosition(-1.0d, -7.0d, 3.0d);

        File check = new File("check.png");
        File barrel = new File("barrel_bottom.png");
        File debug = new File("debug.png");
        File cobble = new File("cobblestone.png");
        File gilded = new File("gilded_blackstone.png");
        File emerald = new File("emerald_block.png");
        File log = new File("spruce_log_top.png");
        File leaves = new File("cherry_leaves.png");
        File sand = new File("sand.png");
        File sandNormal = new File("sand_n.png");
        File terracotta = new File("terracotta.png");
        File cyan = new File("cyan_glazed_terracotta.png");
        File uv = new File("uv.png");
        File bricks = new File("stone_bricks.png");
        File bricksNormal = new File("stone_bricks_n.png");

        MarchingLight[] lights = new MarchingLight[3];
        // lights[2] = new DirectionalLight(new double[]{-0.7d, 0.3d, 0.1d}, new Color(255, 255, 255), 2);
        // lights[1] = new DirectionalLight(new double[]{0.0d, 0.7d, 0.0d}, new Color(255, 255, 255), 500);
        // lights[0] = new DirectionalLight(new double[]{0.0d, 0.75d, -0.1d}, new Color(255, 255, 255), 500);
        lights[1] = new SquareLight(new double[]{-1.0d, -1.0d, 10.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 2700000, 20.0d);
        lights[0] = new DiskLight(new double[]{0.0d, -0.2d, 5.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 120000, 5.0d);

        MarchingObject[] objects = new MarchingObject[7];
        objects[6] = new Box(new TextureMaterial(bricks, bricksNormal), new double[]{0.0d, 0.0d, 0.0d}, new double[]{2.0d, 2.0d, 2.0d});
        // objects[5] = new Torus(new TextureMaterial(cyan), new double[]{0.0d, 0.0d, 0.0d}, 0.5d, 1.0d);
        objects[4] = new Plane(new TextureMaterial(sand, sandNormal), new double[]{0.0d, 0.0d, -0.5d}, new double[]{0.0d, 0.0d, 0.0d}, 'z');
        objects[3] = new Sphere(new TextureMaterial(leaves), new double[]{2.0d, 2.0d, 0.0d}, 1.0f);
        objects[2] = new Sphere(new TextureMaterial(gilded), new double[]{-2.0d, 2.0d, 0.0d}, 1.0f);
        objects[1] = new Sphere(new TextureMaterial(log), new double[]{2.0d, -2.0d, 0.0d}, 1.0f);
        objects[0] = new Sphere(new TextureMaterial(terracotta), new double[]{-2.0d, -2.0d, 0.0d}, 1.0f);
        // objects[0] = new Fractal(new Material(new Color(121, 225, 194)), new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});

        MarchingScene scene = new MarchingScene(objects, lights);

        MarchingRenderer renderer = new MarchingRenderer(scene, camera, SPP);

        Runnable[] tasks = new Runnable[width];

        int i = 0;
        while (i == 0) {
            i++;

            Integer[] order = new Integer[width];

            for (int o = 0; o < width; o++) {
                order[o] = o;
            }

            List<Integer> orderList = Arrays.asList(order);
            Collections.shuffle(orderList);
            order = orderList.toArray(new Integer[width]);

            for (int x = 0; x < width; x++) {
                tasks[x] = new RenderTask(x, renderer, ui);
            }

            ExecutorService pool = Executors.newFixedThreadPool(8);
            long startTime = System.nanoTime();

            for (int x = 0; x < width; x++) {
                pool.execute(tasks[order[x]]);
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

        if (save) {
            File file = new File("render.png");

            for (int r = 1; file.exists(); r++) {
                file = new File(String.format("render_%d.png", r));
            }

            ui.saveRender(file.getName());
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