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
import org.wolfboy.renderer.marching.objects.MarchingObject;
import org.wolfboy.renderer.marching.objects.primitive.Box;
import org.wolfboy.renderer.marching.objects.primitive.Plane;
import org.wolfboy.renderer.marching.objects.primitive.Sphere;
import org.wolfboy.ui.UI;

import java.awt.*;
import java.io.File;
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
        final boolean save = false;
        final int SPP = 64;

        UI ui = new UI(width, height);
        MarchingCamera camera = new MarchingCamera(width, height, 1.2d, 6.75, 0.0d);
        camera.setRotation(-0.5d, 0.0d, 0.2d);
        camera.setPosition(-1.0d, -7.0d, 3.0d);

        File check = new File("check.png");
        File barrel = new File("barrel_side.png");
        File blackConcrete = new File("black_concrete.png");
        File blackGlass = new File("black_stained_glass.png");
        File obsidianCrying = new File("crying_obsidian.png");
        File lodestone = new File("lodestone_top.png");
        File netherite = new File("netherite_block.png");
        File respawn = new File("respawn_anchor_top_off.png");
        File stoneBricks = new File("stone_bricks.png");
        File stoneBricksMossy = new File("stonebrick_mossy.png");
        File anvil = new File("anvil_base.png");
        File blackShulker = new File("black_shulker_box.png");
        File grayShulker = new File("gray_shulker_box.png");
        File greenShulker = new File("green_shulker_box.png");
        File lightGrayShulker = new File("light_gray_shulker_box.png");
        File limeShulker = new File("lime_shulker_box.png");
        File clayGreen = new File("hardened_clay_stained_green.png");
        File clayLime = new File("hardened_clay_stained_lime.png");
        File smithingTable = new File("smithing_table_top.png");

        MarchingLight[] lights = new MarchingLight[3];
        lights[2] = new DirectionalLight(new double[]{-0.1d, 0.65d, 0.1d}, new Color(255, 255, 255), 200);
        //lights[1] = new DirectionalLight(new double[]{0.0d, 0.7d, 0.0d}, new Color(255, 255, 255), 500);
        //lights[0] = new DirectionalLight(new double[]{0.0d, 0.75d, -0.1d}, new Color(255, 255, 255), 500);
        // lights[1] = new DiskLight(new double[]{0.0d, 1.0d, 3.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 200000000, 10.0d);
        lights[0] = new DiskLight(new double[]{0.0d, 0.0d, 5.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 50000000, 5.0d);

        MarchingObject[] objects = new MarchingObject[6];
        objects[5] = new Box(new SolidMaterial(new Color(225, 157, 128)), new double[]{0.0d, 0.0d, 0.0d}, new double[]{2.0d, 2.0d, 2.0d});
        objects[4] = new Plane(new TextureMaterial(clayLime), new double[]{0.0d, 0.0d, -0.5d}, new double[]{0.0d, 0.0d, 0.0d}, 'z');
        objects[3] = new Sphere(new SolidMaterial(new Color(142, 202, 125)), new double[]{2.0d, 2.0d, 0.0d}, 1.0f);
        objects[2] = new Sphere(new SolidMaterial(new Color(202, 190, 72)), new double[]{-2.0d, 2.0d, 0.0d}, 1.0f);
        objects[1] = new Sphere(new SolidMaterial(new Color(80, 109, 154)), new double[]{2.0d, -2.0d, 0.0d}, 1.0f);
        objects[0] = new Sphere(new SolidMaterial(new Color(202, 82, 192)), new double[]{-2.0d, -2.0d, 0.0d}, 1.0f);
        // objects[0] = new Fractal(new Material(new Color(121, 225, 194)), new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});

        MarchingScene scene = new MarchingScene(objects, lights);

        MarchingRenderer renderer = new MarchingRenderer(scene, camera, SPP);

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