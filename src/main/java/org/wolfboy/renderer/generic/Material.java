package org.wolfboy.renderer.generic;

public interface Material {

    double[] getAlbedo(double[] p, double[] uv);

    double[] getNormal(double[] p, double[] uv);

    double getDisplacement(double[] p, double[] uv);

    double getRoughness(double[] p, double[] uv);

    double getSpecular(double[] p, double[] uv);

    double getMetalic(double[] p, double[] uv);

    double getTransmission(double[] p, double[] uv);

    double getEmission(double[] p, double[] uv);
}
