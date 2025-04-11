package org.wolfboy.renderer.generic;

import org.wolfboy.LinearAlgebra;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TextureMaterial implements Material {

    private double scale;
    private BufferedImage albedo;
    private BufferedImage normal;
    private BufferedImage roughness;
    private BufferedImage specular;
    private BufferedImage metalic;
    private BufferedImage transmission;

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
        if (normal == null) {
            return new double[]{0.0d, 0.0d, 0.0d};
        }

        double[] n = this.getColorAtUV(this.normal, uv);
        n = LinearAlgebra.sub(LinearAlgebra.mul(n, 2.0d), 1.0d);
        return n;
    }

    @Override
    public float getRoughness(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getSpecular(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getMetalic(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getTransmission(double[] p, double[] uv) {
        return 0;
    }

    @Override
    public float getEmission(double[] p, double[] uv) {
        return 0;
    }

    private double[] getColorAtUV(BufferedImage image, double[] uv) {
        uv = new double[]{uv[0] * this.scale, uv[1] * this.scale};
        int x = (int) (uv[0] * (image.getWidth()));
        int y = (int) (uv[1] * (image.getHeight()));
        x = Math.min(x, image.getWidth() - 1);
        y = Math.min(y, image.getHeight() - 1);

        int color = 0;
        try {
            color = image.getRGB(x, y);
            // System.out.println(uv[0] * (this.albedo.getWidth()) + ", " + uv[1] * (this.albedo.getHeight()));
        } catch (Exception e) {
            System.err.println(uv[0] * (image.getWidth()) + ", " + uv[1] * (image.getHeight()));
        }

        return new double[]{((color & 0x00ff0000) >> 16) / 255.0d, ((color & 0x0000ff00) >> 8) / 255.0d, (color & 0x000000ff) / 255.0d};
    }
}
