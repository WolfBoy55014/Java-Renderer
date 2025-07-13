package org.wolfboy;

public class Vector3 {

    public double x; // theta
    public double y; // phi
    public double z; // r

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vector3(double[] vector) {
        this.x = vector[0];
        this.y = vector[1];
        this.z = vector[2];
    }

    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2.0d) + Math.pow(this.y, 2.0d) + Math.pow(this.z, 2.0d));
    }

    public Vector3 add(Vector3 vector) {
        return new Vector3(
                this.x + vector.x,
                this.y + vector.y,
                this.z + vector.z
        );
    }

    public Vector3 add(double value) {
        return new Vector3(
                this.x + value,
                this.y + value,
                this.z + value
        );
    }

    public Vector3 sub(Vector3 vector) {
        return new Vector3(
                this.x - vector.x,
                this.y - vector.y,
                this.z - vector.z
        );
    }

    public Vector3 sub(double value) {
        return new Vector3(
                this.x - value,
                this.y - value,
                this.z - value
        );
    }

    public Vector3 mul(Vector3 vector) {
        return new Vector3(
                this.x * vector.x,
                this.y * vector.y,
                this.z * vector.z
        );
    }

    public Vector3 mul(double value) {
        return new Vector3(
                this.x * value,
                this.y * value,
                this.z * value
        );
    }

    public Vector3 div(Vector3 vector) {
        return new Vector3(
                this.x / vector.x,
                this.y / vector.y,
                this.z / vector.z
        );
    }

    public Vector3 div(double value) {
        return new Vector3(
                this.x / value,
                this.y / value,
                this.z / value
        );
    }

    public Vector3 pow(Vector3 vector) {
        return new Vector3(
                this.x = Math.pow(this.x, vector.x),
                this.y = Math.pow(this.y, vector.y),
                this.z = Math.pow(this.z, vector.z)
        );
    }

    public Vector3 pow(double value) {
        return new Vector3(
                this.x = Math.pow(this.x, value),
                this.y = Math.pow(this.y, value),
                this.z = Math.pow(this.z, value)
        );
    }

    public double accumulate() {
        return this.x + this.y + this.z;
    }

    public void normalize() {
        Vector3 t = this.div(this.magnitude());
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    public double distance(Vector3 vector) {
        return Math.sqrt(Math.pow((this.x - vector.x), 2.0d) + Math.pow((this.y - vector.y), 2.0d) + Math.pow((this.z - vector.z), 2.0d));
    }

    public double dot(Vector3 vector) {
        return (this.x * vector.x) + (this.y * vector.y) + (this.z * vector.z);
    }

    public  Vector3 rot(double angle) {
        // Rotation is expensive, so check if we need to first
        if (angle == 0.0d) {
            return this.clone();
        }

        double[] vector = new double[]{this.x, this.y, this.z};

        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double[][] rot = new double[][]{{c, -s}, {s, c}};

        double[] result = new double[]{0.0d, 0.0d};

        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                result[j] += vector[k] * rot[k][j];
            }
        }

        return new Vector3(result);
    }

    public Vector3 abs() {
        return new Vector3(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    public Vector3 mod(double value) {
        //  x - y * floor(x / y)
        return new Vector3(this.x - value * Math.floor(this.x / value),
                           this.y - value * Math.floor(this.y / value),
                           this.z - value * Math.floor(this.z / value));
    }

    public Vector3 sphericalToCartesian() {
        double x = this.z * Math.sin(this.x) * Math.cos(this.y);
        double y = this.z * Math.sin(this.x) * Math.sin(this.y);
        double z = this.z * Math.cos(this.x);

        return new Vector3(x, y, z);
    }

    public Vector3 cartesianToSpherical() {
        double r = this.magnitude();
        double theta = Math.acos(this.z / r);
        double phi = Math.atan2(this.y, this.x);

        return new Vector3(theta, phi, r);
    }

//    public Vector3 toLocal(Vector3 position, Vector3 rotation, Vector3 scale) {
//        // Transform point to account for object position, rotation, and scale
//        Vector3 p = this.clone();
//        p = p.sub(position);
//
//        if (rotation.x != 0.0d | rotation.y != 0.0d | rotation.z != 0.0d) {
//
//            double[] roll = LinearAlgebra.rot(new double[]{p.x, p.z}, rotation.y);
//            p.x = roll.x;
//            p.z = roll.y;
//
//            double[] pitch = LinearAlgebra.rot(new double[]{p.y, p.z}, -rotation.x);
//            p.y = pitch.x;
//            p.z = pitch.y;
//
//            double[] yaw = LinearAlgebra.rot(new double[]{p.x, p.y}, rotation.z);
//            p.x = yaw.x;
//            p.y = yaw.y;
//
//        }
//
//        if (scale.x != 1.0d && scale.y != 1.0d && scale.z != 1.0d) {
//            p = LinearAlgebra.div(p, scale);
//        }
//        return p;
//    }
}
