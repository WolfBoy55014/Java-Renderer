package org.wolfboy.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class ExtendedImage extends BufferedImage {
    public ExtendedImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public ExtendedImage(int width, int height, int imageType, IndexColorModel cm) {
        super(width, height, imageType, cm);
    }

    public ExtendedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        super(cm, raster, isRasterPremultiplied, properties);
    }

    public double[] getDoubleColor(int x, int y) {
        x = this.clamp(x, this.getWidth());
        y = this.clamp(y, this.getHeight());
        int color = this.getRGB(x, y);
        return new double[]{((color & 0x00ff0000) >> 16) / 255.0d, ((color & 0x0000ff00) >> 8) / 255.0d, (color & 0x000000ff) / 255.0d};
    }

    public Color getColor(int x, int y) {
        x = this.clamp(x, this.getWidth());
        y = this.clamp(y, this.getHeight());
        int color = this.getRGB(x, y);
        return new Color(color);
    }

    public void setColor(int x, int y, double[] color) {
        x = this.clamp(x, this.getWidth());
        y = this.clamp(y, this.getHeight());
        Color c = new Color((int) (color[0] * 255), (int) (color[1] * 255), (int) (color[2] * 255));
        this.setRGB(x, y, c.getRGB());
    }

    public void setColor(int x, int y, Color color) {
        x = this.clamp(x, this.getWidth());
        y = this.clamp(y, this.getHeight());
        this.setRGB(x, y, color.getRGB());
    }

    private int clamp(int i, int max) {
        return Math.max(Math.min(i, max - 1), 0);
    }
}
