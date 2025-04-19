package org.wolfboy.renderer.generic;

import org.wolfboy.LinearAlgebra;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Arrays;

public class TextureMaterial implements Material {

    private double scale;
    private BufferedImage albedo;
    private BufferedImage normal;
    private BufferedImage displacement;
    private BufferedImage roughness;
    private BufferedImage specular;
    private BufferedImage metalic;
    private BufferedImage transmission;
    private boolean interpolate = false;

    public TextureMaterial(double scale, File albedo, File normal, File metallic, File roughness) {
        this.scale = scale;

        try {
            this.albedo = ImageIO.read(albedo);
            this.normal = ImageIO.read(normal);
            this.metalic = ImageIO.read(metallic);
            this.roughness = ImageIO.read(roughness);
        } catch (IOException e) {
            System.err.println("Error opening image: " + e.getMessage());
        }
    }

    public TextureMaterial(double scale, File albedo, File normal, File displacement) {
        this.scale = scale;

        try {
            this.albedo = ImageIO.read(albedo);
            this.normal = ImageIO.read(normal);
            this.displacement = ImageIO.read(displacement);
        } catch (IOException e) {
            System.err.println("Error opening image: " + e.getMessage());
        }
    }

    public TextureMaterial(double scale, File albedo, File normal) {
        this.scale = scale;

        try {
            this.albedo = ImageIO.read(albedo);
            this.normal = ImageIO.read(normal);
        } catch (IOException e) {
            System.err.println("Error opening image: " + e.getMessage());
        }
    }

    public TextureMaterial(double scale, File albedo) {
        this.scale = scale;

        try {
            this.albedo = ImageIO.read(albedo);
        } catch (IOException e) {
            System.err.println("Error opening image: " + e.getMessage());
        }
    }

    @Override
    public double[] getAlbedo(double[] p, double[] uv) {
        return this.getColorAtUV(this.albedo, uv);
    }

    @Override
    public double[] getNormal(double[] p, double[] uv) {
        if (this.normal == null) {
            return new double[]{0.0d, 0.0d, 0.0d};
        }

        double[] n = this.getColorAtUV(this.normal, uv);
        n = LinearAlgebra.sub(LinearAlgebra.mul(n, 2.0d), 1.0d);
        return n;
    }

    @Override
    public double getDisplacement(double[] p, double[] uv) {
        if (this.displacement == null) {
            return 0.0d;
        }

        double[] h = this.getColorAtUV(this.displacement, uv);
        return h[0];
    }

    @Override
    public double getRoughness(double[] p, double[] uv) {
        if (this.roughness == null) {
            return 0.0d;
        }

        double[] r = this.getColorAtUV(this.roughness, uv);
        return r[0];
    }

    @Override
    public double getSpecular(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public double getMetalic(double[] p, double[] uv) {
        if (this.metalic == null) {
            return 0.0d;
        }

        double[] m = this.getColorAtUV(this.metalic, uv);
        return m[0];
    }

    @Override
    public double getTransmission(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public double getEmission(double[] p, double[] uv) {
        return 0;
    }

    private double[] getColorAtUV(BufferedImage image, double[] uv) {
        uv = new double[]{uv[0] * this.scale, uv[1] * this.scale};
        int x = (int) (uv[0] * (image.getWidth()));
        int y = (int) (uv[1] * (image.getHeight()));
        x = (int) ((Math.abs(x) * this.scale) % image.getWidth());
        y = (int) ((Math.abs(y) * this.scale) % image.getHeight());

        return this.getColorAtPixel(image, x, y);
    }

    private double[] getColorAtPixel(BufferedImage image, int x, int y) {
        x = x % image.getWidth();
        y = y % image.getHeight();
        int color = image.getRGB(x, y);
        return new double[]{((color & 0x00ff0000) >> 16) / 255.0d, ((color & 0x0000ff00) >> 8) / 255.0d, (color & 0x000000ff) / 255.0d};
    }
}
