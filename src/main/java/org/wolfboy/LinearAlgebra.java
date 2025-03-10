package org.wolfboy;

public class LinearAlgebra {

    /**
     * Calculate the magnitude of a 3-dimensional vector
     *
     * @param vector Vector to calculate the length of
     * @return Magnitude/length of vector
     */
    public static double magnitude(double[] vector) {
        return Math.sqrt(Math.pow(vector[0], 2.0d) + Math.pow(vector[1], 2.0d) + Math.pow(vector[2], 2.0d));
    }

    /**
     * Element-wise addition of two 3-dimensional vectors
     *
     * @param v1 First vector
     * @param v2 Second vector
     * @return 3-dimensional vector containing the sum of v1 and v2
     */
    public static double[] add(double[] v1, double[] v2) {
        return new double[]{v1[0] + v2[0], v1[1] + v2[1], v1[2] + v2[2]};
    }

    /**
     * Element-wise subtraction of two 3-dimensional vectors
     *
     * @param v1 First vector
     * @param v2 Second vector
     * @return 3-dimensional vector containing the subtraction of v1 and v2
     */
    public static double[] sub(double[] v1, double[] v2) {
        return new double[]{v1[0] - v2[0], v1[1] - v2[1], v1[2] - v2[2]};
    }

    /**
     * Element-wise multiplication of two 3-dimensional vectors
     *
     * @param v1 First vector
     * @param v2 Second vector
     * @return 3-dimensional vector containing the multiplication of v1 by v2
     */
    public static double[] mul(double[] v1, double[] v2) {
        return new double[]{v1[0] * v2[0], v1[1] * v2[1], v1[2] * v2[2]};
    }

    /**
     * Element-wise division of two 3-dimensional vectors
     *
     * @param v1 First vector
     * @param v2 Second vector
     * @return 3-dimensional vector containing the division of v1 by v2
     */
    public static double[] div(double[] v1, double[] v2) {
        return new double[]{v1[0] / v2[0], v1[1] / v2[1], v1[2] / v2[2]};
    }

    /**
     * Element-wise exponent of two 3-dimensional vectors
     *
     * @param v1 First vector
     * @param v2 Second vector
     * @return 3-dimensional vector containing the power of v1 by v2
     */
    public static double[] pow(double[] v1, double[] v2) {
        return new double[]{Math.pow(v1[0], v2[0]), Math.pow(v1[1], v2[1]), Math.pow(v1[2], v2[2])};
    }

    /**
     * Add a scalar value to every element of a 3-dimensional vector
     *
     * @param vector 3-dimensional vector
     * @param value Scalar value
     * @return 3-dimensional vector containing the sum of value to vector
     */
    public static double[] add(double[] vector, double value) {
        return new double[]{vector[0] + value, vector[1] + value, vector[2] + value};
    }

    /**
     * Subtract a scalar value from every element of a 3-dimensional vector
     *
     * @param vector 3-dimensional vector
     * @param value Scalar value
     * @return 3-dimensional vector containing the subtraction of value from vector
     */
    public static double[] sub(double[] vector, double value) {
        return new double[]{vector[0] - value, vector[1] - value, vector[2] - value};
    }

    /**
     * Multiply every element of a 3-dimensional vector by a scalar value
     *
     * @param vector 3-dimensional vector
     * @param value Scalar value
     * @return 3-dimensional vector containing the multiplication of vector by value
     */
    public static double[] mul(double[] vector, double value) {
        return new double[]{vector[0] * value, vector[1] * value, vector[2] * value};
    }

    /**
     * Divide every element of a 3-dimensional vector by a scalar value
     *
     * @param vector 3-dimensional vector
     * @param value Scalar value
     * @return 3-dimensional vector containing the division of vector by value
     */
    public static double[] div(double[] vector, double value) {
        if (value == 0.0d) {
            return new double[]{0.0d, 0.0d, 0.0d};
        }

        return new double[]{vector[0] / value, vector[1] / value, vector[2] / value};
    }

    /**
     * Put every element of a 3-dimensional vector to the power of a scalar value
     *
     * @param vector 3-dimensional vector
     * @param value Scalar value
     * @return 3-dimensional vector containing the vector to the power of value
     */
    public static double[] pow(double[] vector, double value) {
        return new double[]{Math.pow(vector[0], value), Math.pow(vector[1], value), Math.pow(vector[2], value)};
    }

    /**
     * Add every element of a 3-dimensional vector together
     *
     * @param vector 3-dimensional vector
     * @return The sum of all the elements of the vector
     */
    public static double accumulate(double[] vector) {
        return vector[0] + vector[1] + vector[2];
    }

    public static double[] normalize(double[] vector) {
        return div(vector, magnitude(vector));
    }

    public static double distance(double[] v1, double[] v2) {
        return Math.sqrt(accumulate(pow(sub(v1, v2), 2.0d)));
    }

    public static double dot(double[] v1, double[] v2) {
        return accumulate(mul(v1, v2));
    }

    public static int[][] multiply(int[][] matrix1, int[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int cols2 = matrix2[0].length;

        if (cols1 != matrix2.length) {
            throw new IllegalArgumentException("Matrices cannot be multiplied: incompatible dimensions.");
        }

        int[][] resultMatrix = new int[rows1][cols2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return resultMatrix;
    }

    public static double[] rot(double[] vector, double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double[][] rot = new double[][]{{c, -s}, {s, c}};

        double[] result = new double[]{0.0d, 0.0d};

        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                result[j] += vector[k] * rot[k][j];
            }
        }

        return result;
    }

    public static double[] round(double[] vector) {
        return new double[]{Math.round(vector[0]), Math.round(vector[1]), Math.round(vector[2])};
    }

    public static double[] abs(double[] vector) {
        return new double[]{Math.abs(vector[0]), Math.abs(vector[1]), Math.abs(vector[2])};
    }

    public static double[] mod(double[] vector, double value) {
        //  x - y * floor(x / y)
        return new double[]{vector[0] - value * Math.floor(vector[0] / value),
                vector[1] - value * Math.floor(vector[1] / value),
                vector[2] - value * Math.floor(vector[2] / value)};
    }

    public double[] sphericalToCartesian(double[] vector) {
        double x = vector[2] * Math.sin(vector[0]) * Math.cos(vector[1]);
        double y = vector[2] * Math.sin(vector[0]) * Math.sin(vector[1]);
        double z = vector[2] * Math.cos(vector[0]);

        return new double[]{x, y, z};
    }

    public double[] cartesianToSpherical(double[] vector) {
        double r = magnitude(vector);
        double theta = Math.acos(vector[2] / r);
        double phi = Math.atan2(vector[1], vector[0]);

        return new double[]{theta, phi, r};
    }
}
