package org.wolfboy.renderer.marching;

import org.wolfboy.renderer.generic.Camera;

public class MarchingCamera extends Camera {

    public MarchingCamera(int width, int height, double FOV) {
        super(width, height, FOV);
    }

    public MarchingCamera(int width, int height, double FOV, double focalDistance, double aperture) {
        super(width, height, FOV, focalDistance, aperture);
    }
}
