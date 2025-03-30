package org.wolfboy.renderer.generic;

public interface Material {

    double[] getAlbedo(double[] p, double[] uv);

    double[] getNormal(double[] p, double[] uv);

    float getRoughness(double[] p, double[] uv);

    float getSpecular(double[] p, double[] uv);

    float getMetalic(double[] p, double[] uv);

    float getTransmission(double[] p, double[] uv);

    float getEmission(double[] p, double[] uv);
}
