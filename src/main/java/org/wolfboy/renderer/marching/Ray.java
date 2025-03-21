package org.wolfboy.renderer.marching;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.Vector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;

import static org.wolfboy.LinearAlgebra.*;

public class Ray {

    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;
    private DoubleVector direction;
    private DoubleVector position;
    private double distance = 0.0f;
    private int steps = 0;

    public Ray(double[] direction, double[] position) {
//        System.out.println("Direction: " + Arrays.toString(direction));
//        System.out.println("Direction Length: " + direction.length);
//        System.out.println("Loop Bound: " + SPECIES.loopBound(direction.length));
//        System.out.println("Species Length: " + SPECIES.length());

        VectorMask<Double> mask = SPECIES.indexInRange(0, direction.length);
        this.direction = DoubleVector.fromArray(SPECIES, normalize(direction), 0, mask);
        this.position = DoubleVector.fromArray(SPECIES, position, 0, mask);

//        System.out.println("Out: " + Arrays.toString(this.direction.toArray()));
    }

    public Ray() {
        this(new double[]{0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d});
    }

    public double getDistance() {
        return distance;
    }

    public void resetDistance() {
        this.distance = 0.0f;
    }

    public double[] getPosition() {
        return position.toDoubleArray();
    }

    public double[] getDirection() {
        return direction.toDoubleArray();
    }

    public void setPosition(double[] position) {
        this.position = DoubleVector.fromArray(SPECIES, position, 0);;
    }

    public void setDirection(double[] direction) {
        this.direction = DoubleVector.fromArray(SPECIES, direction, 0);;
    }

    public int getSteps() {
        return steps;
    }

    public void step(double distance) {;
        Vector<Double> deltaPose = this.direction.mul(distance);

        this.position = this.position.add(deltaPose);
        this.distance += distance;
//        this.steps++;
    }
}
