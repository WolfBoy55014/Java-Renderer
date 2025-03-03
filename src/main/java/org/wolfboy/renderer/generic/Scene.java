package org.wolfboy.renderer.generic;

public class Scene {

    protected Object[] objects;

    protected Scene(Object[] objects) {
        this.objects = objects;
    }

    protected Object[] getObjects() {
        return objects;
    }

    public void addObject(Object object) {
        Object[] newObjects = new Object[objects.length + 1];
        System.arraycopy(objects, 0, newObjects, 0, objects.length);
        newObjects[objects.length] = object;
        objects = newObjects;
    }
}
