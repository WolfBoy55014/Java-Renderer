package org.wolfboy.renderer.generic;

import org.wolfboy.LinearAlgebra;
import org.wolfboy.renderer.marching.Ray;

public class Camera {

    protected double FOV;
    protected double focalDistance;
    protected double aperture;
    protected int width;
    protected int height;

    protected double[] position = new double[3];
    protected double[] rotation = new double[3];

    public Camera(int width, int height, double FOV) {
        this.width = width;
        this.height = height;
        this.FOV = FOV;
        this.focalDistance = Double.MAX_VALUE;
        this.aperture = 0.0d;
    }

    public Camera(int width, int height, double FOV, double focalDistance, double aperture) {
        this.width = width;
        this.height = height;
        this.FOV = FOV;
        this.focalDistance = focalDistance;
        this.aperture = aperture;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public void setPosition(double x, double y, double z) {
        this.position = new double[]{x, y, z};
    }

    public void setRotation(double[] rotation) {
        this.rotation = rotation;
    }

    public void setRotation(double pitch, double roll, double yaw) {
        this.rotation = new double[]{pitch, roll, yaw};
    }

    public void setFOV(double FOV) {
        this.FOV = FOV;
    }

    public double getFOV() {
        return FOV;
    }

    public double[] getRotation() {
        return rotation;
    }

    public double[] getPosition() {
        return position;
    }

    public Ray getRayAtPixel(int x, int y) {

        double[] direction = new double[3];
        double[] position = this.position;

        // Initial direction

        // Calculate the horizontal angle (theta)
        //double theta = ((double) x / width) * this.FOV;
        double theta = ((double) x / width) * this.FOV - (this.FOV / 2.0);

        // Calculate the aspect ratio
        double aspectRatio = (double) width / height;

        // Calculate the vertical angle (phi), now considering the FOV and aspect ratio
        double phi = ((double) y / this.height) * this.FOV / aspectRatio - (this.FOV / (2 * aspectRatio));

        // System.out.println("Theta: " + theta + " Phi: " + phi);

        // Calculate the ray direction
        direction[1] = Math.cos(theta) * Math.cos(phi);
        direction[2] = -Math.sin(phi);
        direction[0] = -Math.sin(theta) * Math.cos(phi);

        // Calculate DOF
        if (this.aperture > 0.0d) {
            double[] convergence = LinearAlgebra.add(this.position, LinearAlgebra.mul(direction, this.focalDistance));
            position = LinearAlgebra.add(position, LinearAlgebra.mul(new double[]{Math.random(), 0.0d, Math.random()}, this.aperture));
            direction = LinearAlgebra.sub(convergence, position);
        }

        // System.out.println(Arrays.toString(direction));

        // Apply Camera rotation
        double[] roll = LinearAlgebra.rot(new double[]{direction[0], direction[2]}, this.rotation[1]);
        direction[0] = roll[0];
        direction[2] = roll[1];

        double[] pitch = LinearAlgebra.rot(new double[]{direction[1], direction[2]}, -this.rotation[0]);
        direction[1] = pitch[0];
        direction[2] = pitch[1];

        double[] yaw = LinearAlgebra.rot(new double[]{direction[0], direction[1]}, this.rotation[2]);
        direction[0] = yaw[0];
        direction[1] = yaw[1];

        // System.out.println(Arrays.toString(direction));

        return new Ray(direction, position);
    }

    public int[] directionToPixel(double[] direction) {
        int x = (int) ((direction[0] / FOV) * this.height + (this.width / 2.0d));
        int y = (int) ((-direction[2] / FOV) * this.height + (this.height / 2.0d));
        return new int[]{x, y};
    }
}
