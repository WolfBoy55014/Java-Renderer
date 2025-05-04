package org.wolfboy;

import org.wolfboy.renderer.generic.SolidMaterial;
import org.wolfboy.renderer.generic.TextureMaterial;
import org.wolfboy.renderer.marching.MarchingCamera;
import org.wolfboy.renderer.marching.MarchingRenderer;
import org.wolfboy.renderer.marching.MarchingScene;
import org.wolfboy.renderer.marching.lights.*;
import org.wolfboy.renderer.marching.objects.Fractal;
import org.wolfboy.renderer.marching.objects.MarchingObject;
import org.wolfboy.renderer.marching.objects.primitive.Box;
import org.wolfboy.renderer.marching.objects.primitive.Plane;
import org.wolfboy.renderer.marching.objects.primitive.Sphere;
import org.wolfboy.renderer.marching.objects.primitive.Torus;
import org.wolfboy.ui.ExtendedImage;
import org.wolfboy.ui.UI;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        final int SPP = 8;
        final double NOISE_THREASHOLD = 0.1;
        final int NUM_THREADS = 6;

        UI ui = new UI(width, height);
        ExtendedImage noiseMap = new ExtendedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        MarchingCamera camera = new MarchingCamera(width, height, 1.2d, 6.75, 0.00d);
        camera.setRotation(-0.5d, 0.0d, 0.2d);
        camera.setPosition(-1.0d, -7.0d, 3.0d);

        File skybox = new File("materials/kloofendal_48d_partly_cloudy_puresky_2k.jpg");
        File check = new File("materials/check.png");
        File uv = new File("materials/uv.png");
        File tile = new File("materials/glossy-marble-tile_albedo.png");
        File tileNormal = new File("materials/glossy-marble-tile_normal-ogl.png");
        File wood = new File("materials/older-wood-flooring_albedo.png");
        File woodNormal = new File("materials/older-wood-flooring_normal-ogl.png");
        File mortar = new File("materials/sloppy-mortar-stone-wall_albedo.png");
        File mortarNormal = new File("materials/sloppy-mortar-stone-wall_normal-ogl.png");
        File space = new File("materials/filthy-space-panels_albedo.png");
        File spaceNormal = new File("materials/filthy-space-panels_normal-ogl.png");
        File spaceMetallic = new File("materials/filthy-space-panels_metallic.png");
        File spaceRoughness = new File("materials/filthy-space-panels_roughness.png");
        File siding = new File("materials/stone-house-siding_albedo.png");
        File sidingNormal = new File("materials/stone-house-siding_normal-ogl.png");
        File steel = new File("materials/used-stainless-steel_albedo.png");
        File steelNormal = new File("materials/used-stainless-steel_normal-ogl.png");
        File steelMetallic = new File("materials/used-stainless-steel_metallic.png");
        File steelRoughness = new File("materials/used-stainless-steel_roughness.png");

        MarchingLight[] lights = new MarchingLight[3];
        // lights[2] = new PointLight(new double[]{0.0d, 0.0d, 5.0d}, new Color(255, 255, 255), 2000);
        // lights[2] = new DirectionalLight(new double[]{-0.7d, 0.3d, 0.1d}, new Color(255, 255, 255), 2);
        // lights[1] = new DirectionalLight(new double[]{0.0d, 0.7d, 0.0d}, new Color(255, 255, 255), 500);
        // lights[0] = new DirectionalLight(new double[]{0.0d, 0.75d, -0.1d}, new Color(255, 255, 255), 500);
        lights[1] = new SquareLight(new double[]{-1.0d, -1.0d, 10.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 2700000, 20.0d);
        lights[0] = new DiskLight(new double[]{0.0d, -0.2d, 5.0d}, new double[]{0.0d, 0.0d, 0.0d}, new Color(255, 255, 255), 120000, 5.0d);

        MarchingObject[] objects = new MarchingObject[6];
        // objects[6] = new Box(new TextureMaterial(1.0d, bricks, bricksNormal), new double[]{0.0d, 0.0d, 0.0d}, new double[]{2.0d, 2.0d, 2.0d});
        objects[5] = new Torus(new SolidMaterial(new Color(134, 255, 184), 0.0d, 0.1d, 0.9d), new double[]{0.0d, 0.0d, 0.0d}, 0.5d, 1.0d);
        objects[4] = new Plane(new TextureMaterial(1.0d, check), new double[]{0.0d, 0.0d, -0.5d}, new double[]{0.0d, 0.0d, 0.0d}, 'z');
        objects[3] = new Sphere(new TextureMaterial(1.0d, uv), new double[]{2.0d, 2.0d, 0.0d}, 1.0f);
        objects[2] = new Sphere(new SolidMaterial(new Color(255, 236, 173), 0.0d, 0.05d, 0.9d), new double[]{-2.0d, 2.0d, 0.0d}, 1.0f);
        objects[1] = new Sphere(new SolidMaterial(new Color(255, 255, 255), 0.99d, 0.01d, 0.0d), new double[]{2.0d, -2.0d, 0.0d}, 1.0f);
        objects[0] = new Sphere(new TextureMaterial(1.0d, space, spaceNormal, spaceMetallic, spaceRoughness), new double[]{-2.0d, -2.0d, 0.0d}, 1.0f);
        // objects[0] = new Fractal(new Material(new Color(121, 225, 194)), new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d}, new double[]{1.0d, 1.0d, 1.0d});

        MarchingScene scene = new MarchingScene(objects, lights);

        MarchingRenderer renderer = new MarchingRenderer(scene, camera);
        renderer.addSkybox(skybox);

        long startTime = System.nanoTime();
        for (int s = 0; s < SPP; s++) {
            Runnable[] tasks = new Runnable[width];

            for (int u = 0; u < width; u++) {
                tasks[u] = new RenderTask(u, s, renderer, ui, noiseMap);
            }

            ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

            for (int i = 0; i < tasks.length; i++) {
                pool.execute(tasks[i]);
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

            ExtendedImage render = ui.getImage();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    double[] upColor = render.getDoubleColor(x, y + 1);
                    double[] downColor = render.getDoubleColor(x, y - 1);
                    double[] leftColor = render.getDoubleColor(x - 1, y);
                    double[] rightColor = render.getDoubleColor(x + 1, y);
                    double[] thisColor = render.getDoubleColor(x, y);

                    double upDot = Math.abs(LinearAlgebra.distance(thisColor, upColor));
                    double downDot = Math.abs(LinearAlgebra.distance(thisColor, downColor));
                    double leftDot = Math.abs(LinearAlgebra.distance(thisColor, leftColor));
                    double rightDot = Math.abs(LinearAlgebra.distance(thisColor, rightColor));

                    double totalDifference = (upDot + downDot + leftDot + rightDot) / (4.0d * Math.sqrt(2.0d));
                    totalDifference = Math.min(totalDifference, 1.0d);
                    // System.out.println(totalDifference);

                    noiseMap.setColor(x, y, new double[]{totalDifference, totalDifference, totalDifference});
                }
            }

            System.out.println("Sample " + s + " Complete");
        }

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  // divide by 1000000 to get milliseconds.

        System.out.println("Time taken: " + duration / 1000000 + "ms");

        ui.display();

        if (save) {
            File file = new File("renders/render.png");

            for (int r = 1; file.exists(); r++) {
                file = new File(String.format("renders/render_%d.png", r));
            }

            ui.saveRender(file.getPath());
        }
    }
}

class RenderTask implements Runnable {

    private final int height;
    private final int width;
    private final int x;
    private final int sample;
    private final MarchingRenderer renderer;
    private final ExtendedImage noiseMap;
    private final UI ui;

    public RenderTask(int x, int sample, MarchingRenderer renderer, UI ui, ExtendedImage noiseMap) {
        this.renderer = renderer;
        this.ui = ui;
        this.x = x;
        this.sample = sample;
        this.height = renderer.getCamera().getHeight();
        this.width = renderer.getCamera().getWidth();
        this.noiseMap = noiseMap;
    }

    public void run() {
        for (int y = 0; y < this.height; y++) {
            double noise = this.noiseMap.getDoubleColor(x, y)[0];
            if (noise >= 0.001d | sample == 0) {
                Color color = renderer.renderPixel(x, y);
                color = mixColors(ui.getPixel(x, y), color, (1.0d / (this.sample + 1)));
                ui.drawPixel(x, y, color);
            }
        }
    }

    private Color mixColors(Color color1, Color color2, double factor) {
        double[] first = new double[]{color1.getRed() / 255.0d, color1.getGreen() / 255.0d, color1.getBlue() / 255.0d};
        double[] second = new double[]{color2.getRed() / 255.0d, color2.getGreen() / 255.0d, color2.getBlue() / 255.0d};
        double[] mixed = LinearAlgebra.mix(first, second, factor);

        return new Color((int) (mixed[0] * 255), (int) (mixed[1] * 255), (int) (mixed[2] * 255));
    }
}