package top.bultrail.markroad.calculate;


import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.DenseMatrixMultiD;
import org.ujmp.core.Matrix;
import org.ujmp.core.doublematrix.DenseDoubleMatrix2D;
import org.ujmp.core.matrix.factory.DenseMatrixFactory;

import java.util.Arrays;


/** MIP example with a variable array. */
public class cal_LP {
//    static class DataModel {
//        public final double[][] constraintCoeffs = {
//                {5, 7, 9, 2, 1},
//                {18, 4, -9, 10, 12},
//                {4, 7, 3, 8, 5},
//                {5, 13, 16, 3, -7},
//        };
//        // 将二维数组转换为 UJMP 矩阵
//        Matrix constraintCoeffs_matrix = Matrix.Factory.importFromArray(constraintCoeffs);
//        // 对每个元素取反
//        Matrix A = constraintCoeffs_matrix.times(-1);
//        // Ap数目
//        int Ap_size = constraintCoeffs[0].length;
//        // sensor数目
//        int sensor_size = constraintCoeffs.length;
//        // 约束条件
//        Matrix bounds_matrix = DenseMatrix.Factory.ones(1, Ap_size);
////        public final double[] bounds = {250, 285, 211, 315};
//        public final double[] bounds = bounds_matrix.toDoubleArray()[0];
//        // 目标函数系数
//        Matrix objCoeffs_matrix = bounds_matrix.times(-1);
//        public final double[] objCoeffs = objCoeffs_matrix.toDoubleArray()[0];
//        // 变量数目
//        public final int numVars = Ap_size;
//        // 约束条件数目
//        public final int numConstraints = sensor_size;
//
//    }

    public static class DataModel {
        // 系数矩阵
        private double[][] constraintCoeffs;
        private Matrix constraintCoeffsMatrix;
        private Matrix A;

        // sensor、Ap数目
        private int ApSize;
        private int sensorSize;

        // 约束条件
        private Matrix boundsMatrix;
        private double[] bounds;

        // 目标函数
        private Matrix objCoeffsMatrix;
        private double[] objCoeffs;

        // 变量数目
        private int numVars;
        // 约束数目
        private int numConstraints;

        public DataModel(double[][] constraintCoeffs) {
            this.constraintCoeffs = constraintCoeffs;
            this.constraintCoeffsMatrix = Matrix.Factory.importFromArray(constraintCoeffs);
//            this.A = constraintCoeffsMatrix.times(-1);
//            this.A = constraintCoeffsMatrix;

            this.ApSize = constraintCoeffs[0].length;
            this.sensorSize = constraintCoeffs.length;

//            this.boundsMatrix = DenseMatrix.Factory.ones(1, sensorSize).times(-1);
            this.boundsMatrix = DenseMatrix.Factory.ones(1, sensorSize);
            this.bounds = boundsMatrix.toDoubleArray()[0];

            this.objCoeffsMatrix = DenseMatrix.Factory.ones(1, ApSize);
            this.objCoeffs = objCoeffsMatrix.toDoubleArray()[0];

            this.numVars = ApSize;
            this.numConstraints = sensorSize;
        }
    }


    public static int[] linprog(double[][] Coverage) throws RuntimeException {
        Loader.loadNativeLibraries();
        final DataModel data = new DataModel(Coverage);

        // Create the linear solver with the SCIP backend.
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            System.out.println("Could not create solver SCIP");
            throw new RuntimeException("Could not create solver SCIP");
        }

        double infinity = java.lang.Double.POSITIVE_INFINITY;
        MPVariable[] x = new MPVariable[data.numVars];
        for (int j = 0; j < data.numVars; ++j) {
//            x[j] = solver.makeIntVar(0.0, infinity, "");
            x[j] = solver.makeIntVar(0.0, 1.0, "");
        }
        System.out.println("Number of variables = " + solver.numVariables());

        // Create the constraints.
        for (int i = 0; i < data.numConstraints; ++i) {
//            MPConstraint constraint = solver.makeConstraint(0, data.bounds[i], "");
            MPConstraint constraint = solver.makeConstraint(data.bounds[i], infinity, "");
            for (int j = 0; j < data.numVars; ++j) {
                constraint.setCoefficient(x[j], data.constraintCoeffs[i][j]);
            }
        }
        System.out.println("Number of constraints = " + solver.numConstraints());

        MPObjective objective = solver.objective();
        for (int j = 0; j < data.numVars; ++j) {
            objective.setCoefficient(x[j], data.objCoeffs[j]);
        }
        // 最小化目标函数
        objective.setMinimization();
        final MPSolver.ResultStatus resultStatus = solver.solve();

        // Check that the problem has an optimal solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Objective value = " + objective.value());
//            for (int j = 0; j < data.numVars; ++j) {
//                System.out.println("x[" + j + "] = " + x[j].solutionValue());
//            }
            System.out.println();
            System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
            System.out.println("Problem solved in " + solver.iterations() + " iterations");
            System.out.println("Problem solved in " + solver.nodes() + " branch-and-bound nodes");
        } else {
            System.err.println("The problem does not have an optimal solution.");
        }
        // 将MPVariable[]数组转换为int[]数组
        int[] x_intArray = new int[x.length];
        for (int i = 0; i < x.length; i++) {
            x_intArray[i] = (int) x[i].solutionValue();
        }
        return x_intArray;
    }

    public static void main(String[] args) throws Exception {
//        double[][] constraintCoeffs = MatConvert.matName2Cover("Coverage.mat");
        double[][] constraintCoeffs = {
            {5, 7, 9, 2, 1},
            {18, 4, -9, 10, 12},
            {4, 7, 3, 8, 5},
            {5, 13, 16, 3, -7},
        };
        int[] result = linprog(constraintCoeffs);
    }
}


