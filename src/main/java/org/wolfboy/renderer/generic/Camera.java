package org.wolfboy.renderer.generic;

import org.wolfboy.LinearAlgebra;

public class Camera {

    protected double FOV;
    protected int width;
    protected int height;

    protected double[] position = new double[3];
    protected double[] rotation = new double[3];

    public Camera(int width, int height, double FOV) {
        this.width = width;
        this.height = height;
        this.FOV = FOV;
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

    public double[] getDirectionAtPixel(int x, int y) {

        double[] direction = new double[3];

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

        //double rayX = Math.sin(theta) * Math.cos(phi);
        //double rayY = Math.sin(phi);
        //double rayZ = Math.cos(theta) * Math.cos(phi);

        //double rayX = Math.cos(theta) * Math.cos(phi); //Swapped sin and cos here.
        //double rayY = Math.sin(phi);
        //double rayZ = -Math.sin(theta) * Math.cos(phi); // Added a negative sign here.

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

        return LinearAlgebra.normalize(direction);
    }

    public int[] directionToPixel(double[] direction) {
        int x = (int) ((direction[0] / FOV) * this.height + (this.width / 2.0d));
        int y = (int) ((-direction[2] / FOV) * this.height + (this.height / 2.0d));
        return new int[]{x, y};
    }
}
