package org.wolfboy.renderer.generic;

import java.awt.*;

public class Renderer {

    protected Scene scene;
    protected Camera camera;
    protected int width;
    protected int height;

    public Renderer(Scene scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
    }

    public Color render(int x, int y) {
        return new Color(0, 0, 0);
    }

    public Scene getScene() {
        return scene;
    }
}
