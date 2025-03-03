package org.wolfboy.renderer.marching;

import java.util.Arrays;

import static org.wolfboy.LinearAlgebra.*;

public class Ray {

    private double[] direction = new double[3];
    private double[] position = new double[3];
    private double distance = 0.0f;

    public Ray(double[] direction, double[] position) {
        this.direction = normalize(direction);
        this.position = position;
    }

    public Ray() {
        this(new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d});
    }

    public double getDistance() {
        return distance;
    }

    public double[] getPosition() {
        return position;
    }

    public double[] getDirection() {
        return direction;
    }

    public double step(double distance) {
        double[] deltaPose = mul(this.direction, distance);

        this.position = add(this.position, deltaPose);
        this.distance += distance;

        return distance;
    }


}
