package org.wolfboy.renderer.marching;

import org.wolfboy.renderer.generic.Scene;
import org.wolfboy.renderer.marching.lights.MarchingLight;
import org.wolfboy.renderer.marching.objects.MarchingObject;

public class MarchingScene extends Scene {

    private final MarchingObject[] objects;
    private final MarchingLight[] lights;

    public MarchingScene(MarchingObject[] objects, MarchingLight[] lights) {
        this.objects = objects;
        this.lights = lights;
    }

    public double getDistance(double[] p) {
        double d = Double.MAX_VALUE;
        for (MarchingObject object : this.objects) {
            if (object == null) {
                continue;
            }

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
            if (object == null) {
                continue;
            }

            double distance = object.getDistance(p);
            // System.out.println("Boo: " + distance);
            if (distance < d) {
                d = distance;
                nearest = object;
            }
        }
        return nearest;
    }

    public MarchingLight[] getLights() {
        return this.lights;
    }
}
