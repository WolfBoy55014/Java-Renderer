package org.wolfboy.renderer.marching;

import uk.ac.manchester.tornado.api.ImmutableTaskGraph;
import uk.ac.manchester.tornado.api.TaskGraph;
import uk.ac.manchester.tornado.api.TornadoExecutionPlan;
import uk.ac.manchester.tornado.api.TornadoExecutionResult;
import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.enums.DataTransferMode;
import uk.ac.manchester.tornado.api.types.arrays.DoubleArray;
import uk.ac.manchester.tornado.api.types.matrix.Matrix2DFloat;

import static org.wolfboy.LinearAlgebra.add;
import static org.wolfboy.LinearAlgebra.mul;

public class Compute {
    private static void _step(DoubleArray position, DoubleArray direction, double dist) {
        DoubleArray deltaPose = new DoubleArray(3);
        for (@Parallel int i = 0; i < 3; i++) {
            deltaPose.set(i, direction.get(i) * dist);
            position.set(i, position.get(i) + deltaPose.get(i));
        }
    }

    public double[] step(double[] position, double[] direction, double dist) {
        DoubleArray _position = DoubleArray.fromArray(position);
        DoubleArray _direction = DoubleArray.fromArray(direction);

        TaskGraph taskGraph = new TaskGraph("s0")
                .transferToDevice(DataTransferMode.EVERY_EXECUTION, _position, _direction, dist) // Transfer data from host to device and mark buffers as read-only,
                // since data will be transferred only during the first execution.
                .task("t0", Compute::_step, _position, _direction, dist)                      // Each task points to an existing Java method
                .transferToHost(DataTransferMode.EVERY_EXECUTION, _position);                     // Transfer data from device to host in every execution.

        // Create an immutable task-graph
        ImmutableTaskGraph immutableTaskGraph = taskGraph.snapshot();

        // Create an execution plan from an immutable task-graph
        TornadoExecutionPlan executionPlan = new TornadoExecutionPlan(immutableTaskGraph);

        // Execute the execution plan
        TornadoExecutionResult executionResult = executionPlan.execute();
        return _position.toHeapArray();
    }
}
