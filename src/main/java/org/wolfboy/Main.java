package org.wolfboy;

import org.wolfboy.renderer.generic.SolidMaterial;
import org.wolfboy.renderer.generic.TextureMaterial;
import org.wolfboy.renderer.marching.MarchingCamera;
import org.wolfboy.renderer.marching.MarchingRenderer;
import org.wolfboy.renderer.marching.MarchingScene;
import org.wolfboy.renderer.marching.lights.*;
import org.wolfboy.renderer.marching.objects.MarchingObject;
import org.wolfboy.renderer.marching.objects.primitive.Box;
import org.wolfboy.renderer.marching.objects.primitive.Plane;
import org.wolfboy.renderer.marching.objects.primitive.Sphere;
import org.wolfboy.renderer.marching.objects.primitive.Torus;
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
        final boolean save = true;
        final int SPP = 128;
        final int tileSize = 2;

        UI ui = new UI(width, height);
        MarchingCamera camera = new MarchingCamera(width, height, 1.2d, 6.75, 0.07d);
        camera.setRotation(-0.5d, 0.0d, 0.2d);
        camera.setPosition(-1.0d, -7.0d, 3.0d);

        File skybox = new File("kloofendal_48d_partly_cloudy_puresky_2k.jpg");
        File check = new File("check.png");
        File uv = new File("uv.png");
        File tile = new File("glossy-marble-tile_albedo.png");
        File tileNormal = new File("glossy-marble-tile_normal-ogl.png");
        File wood = new File("older-wood-flooring_albedo.png");
        File woodNormal = new File("older-wood-flooring_normal-ogl.png");
        File mortar = new File("sloppy-mortar-stone-wall_albedo.png");
        File mortarNormal = new File("sloppy-mortar-stone-wall_normal-ogl.png");
        File space = new File("filthy-space-panels_albedo.png");
        File spaceNormal = new File("filthy-space-panels_normal-ogl.png");
        File siding = new File("stone-house-siding_albedo.png");
        File sidingNormal = new File("stone-house-siding_normal-ogl.png");

        MarchingLight[] lights = new MarchingLight[3];
        // lights[2] = new PointLight(new double[]{0.0d, 0.0d, 5.0d}, new Color(255, 255, 255), 2000);
        // lights[2] = new DirectionalLight(new double[]{-0.7d, 0.3d, 0.1d}, new Color(255, 255, 255), 2);
        // lights[1] = new DirectionalLight(new double[]{0.0d, 0.7d, 0.0d}, new Color(255, 255, 255), 500);
        // lights[0] = new DirectionalLight(new double[]{0.0d, 0.75d, -0.1d}, new Color(255, 255, 255), 500);
        lights[1] = new SquareLight(new double[]{-1.0d, -1.0d, 10.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 2700000, 20.0d);
        lights[0] = new DiskLight(new double[]{0.0d, -0.2d, 5.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 120000, 5.0d);

        MarchingObject[] objects = new MarchingObject[7];
        // objects[6] = new Box(new TextureMaterial(1.0d, bricks, bricksNormal), new double[]{0.0d, 0.0d, 0.0d}, new double[]{2.0d, 2.0d, 2.0d});
        objects[5] = new Torus(new TextureMaterial(1.0d, uv), new double[]{0.0d, 0.0d, 0.0d}, 0.5d, 1.0d);
        objects[4] = new Plane(new TextureMaterial(1.0d, check), new double[]{0.0d, 0.0d, -0.5d}, new double[]{0.0d, 0.0d, 0.0d}, 'z');
        objects[3] = new Sphere(new TextureMaterial(1.0d, uv), new double[]{2.0d, 2.0d, 0.0d}, 1.0f);
        objects[2] = new Sphere(new SolidMaterial(new Color(255, 166, 0), 0.9d, 0.04d), new double[]{-2.0d, 2.0d, 0.0d}, 1.0f);
        objects[1] = new Sphere(new SolidMaterial(new Color(80, 219, 160), 1.0d, 0.02d), new double[]{2.0d, -2.0d, 0.0d}, 1.0f);
        objects[0] = new Sphere(new TextureMaterial(1.0d, uv), new double[]{-2.0d, -2.0d, 0.0d}, 1.0f);
        // objects[0] = new Fractal(new Material(new Color(121, 225, 194)), new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});

        MarchingScene scene = new MarchingScene(objects, lights);

        MarchingRenderer renderer = new MarchingRenderer(scene, camera, SPP);
        renderer.addSkybox(skybox);

        int numTilesX = (int) Math.ceil((double) width / tileSize);
        int numTilesY = (int) Math.ceil((double) height / tileSize);
        int numTiles = numTilesX * numTilesY;

        Runnable[] tasks = new Runnable[numTiles];

        for (int u = 0; u < numTiles; u++) {
            tasks[u] = new RenderTask(u, tileSize, renderer, ui);
        }

        ExecutorService pool = Executors.newFixedThreadPool(12);
        long startTime = System.nanoTime();

        for (int u = 0; u < numTiles; u++) {
            pool.execute(tasks[u]);
        }

        pool.shutdown();

        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ui.display();
            // Wait for all threads to finish
        }

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  // divide by 1000000 to get milliseconds.

        System.out.println("Time taken: " + duration / 1000000 + "ms");

        ui.display();


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
    private final int tile;
    private final int tileSize;
    private final int height;
    private final int width;
    private final MarchingRenderer renderer;
    private final UI ui;
    private final int numTilesX;
    private final int numTilesY;

    public RenderTask(int tile, int tileSize, MarchingRenderer renderer, UI ui) {
        this.renderer = renderer;
        this.ui = ui;
        this.height = renderer.getCamera().getHeight();
        this.width = renderer.getCamera().getWidth();
        this.tile = tile;
        this.tileSize = tileSize;
        this.numTilesX = (int) Math.ceil((double) this.width / tileSize);
        this.numTilesY = (int) Math.ceil((double) this.height / tileSize);
    }

    public void run() {

        int ia = this.tileSize * (this.tile % this.numTilesX);
        int ja = this.tileSize * (this.tile / this.numTilesX);
        int x, y;

        // for every pixel in this tile, compute color
        for (int i = 0; i < this.tileSize; i++) {
            for (int j = 0; j < this.tileSize; j++) {
                x = ia + i;
                y = ja + j;

                if (x >= width) {
                    continue;
                }
                if (y >= height) {
                    continue;
                }

                Color color = renderer.renderPixel(x, y);
                ui.drawPixel(x, y, color);
            }
        }
    }
}