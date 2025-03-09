package org.wolfboy.renderer.generic;

public class Scene {

    protected Object[] objects;
    protected Light[] lights;

    protected Scene(Object[] objects, Light[] lights) {
        this.objects = objects;
        this.lights = lights;
    }

    public Object[] getObjects() {
        return objects;
    }

    public Light[] getLights() {
        return lights;
    }

    public void addObject(Object object) {
        Object[] newObjects = new Object[objects.length + 1];
        System.arraycopy(objects, 0, newObjects, 0, objects.length);
        newObjects[objects.length] = object;
        objects = newObjects;
    }

    public void addLight(Light light) {
        Light[] newLights = new Light[lights.length + 1];
        System.arraycopy(lights, 0, newLights, 0, lights.length);
        newLights[lights.length] = light;
        lights = newLights;
    }
}
