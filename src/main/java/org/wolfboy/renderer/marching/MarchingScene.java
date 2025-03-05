package org.wolfboy.renderer.marching;

import org.wolfboy.renderer.generic.Scene;
import org.wolfboy.renderer.marching.objects.MarchingObject;

public class MarchingScene extends Scene {

    private final MarchingObject[] objects;

    public MarchingScene(MarchingObject[] objects) {
        super(objects);
        this.objects = objects;
    }

    public double getDistance(double[] p) {
        double d = Double.MAX_VALUE;
        for (MarchingObject object : this.objects) {
            d = Math.min(d, object.getDistance(p));
        }
        return d;
    }

    public double[] getNormal(double[] p) {
        return getNearestObject(p).getNormal(p);
    }

    public MarchingObject getNearestObject(double[] p) {
        MarchingObject nearest = null;
        double d = Double.MAX_VALUE;
        for (MarchingObject object : this.objects) {
            double distance = object.getDistance(p);
            if (distance < d) {
                d = distance;
                nearest = object;
            }
        }
        return nearest;
    }
}
