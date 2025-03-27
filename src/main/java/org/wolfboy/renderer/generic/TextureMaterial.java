package org.wolfboy.renderer.generic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureMaterial implements Material {

    private BufferedImage albedo;
    private BufferedImage roughness;
    private BufferedImage specular;
    private BufferedImage metalic;
    private BufferedImage transmission;

    public TextureMaterial(File albedo) {
        try {this.albedo = ImageIO.read(albedo);
        } catch (IOException e) {
            System.err.println("Error opening image: " + e.getMessage());
        }
    }

    @Override
    public double[] getAlbedo(double[] p, double[] uv) {
        int x = (int) (uv[0] * this.albedo.getWidth());
        int y = (int) (uv[1] * this.albedo.getHeight());
        int color = this.albedo.getRGB(x, y);
        return new double[]{((color & 0x00ff0000) >> 16) / 255.0d, ((color & 0x0000ff00) >> 8) / 255.0d, (color & 0x000000ff) / 255.0d};
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
}
