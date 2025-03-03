package org.wolfboy.renderer.generic;

public class Object {

    protected Material material;
    protected double[] position;
    protected double[] rotation;
    protected double[] scale;

    public Object(Material material, double[] position, double[] rotation, double[] scale) {
        this.material = material;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Material getMaterial() {
        return material;
    }

    public double[] getPosition() {
        return position;
    }

    public double[] getRotation() {
        return rotation;
    }

    public double[] getScale() {
        return scale;
    }
}
