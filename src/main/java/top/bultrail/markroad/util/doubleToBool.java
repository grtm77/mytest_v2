package top.bultrail.markroad.util;

public class doubleToBool {

    public static boolean[][] trans(double[][] doubleMatrix){
        boolean[][] booleanMatrix = new boolean[doubleMatrix.length][doubleMatrix[0].length];
        for (int i = 0; i < doubleMatrix.length; i++) {
            for (int j = 0; j < doubleMatrix[0].length; j++) {
                if (doubleMatrix[i][j] > 0) {
                    booleanMatrix[i][j] = true;
                } else {
                    booleanMatrix[i][j] = false;
                }
            }
        }
        return booleanMatrix;
    }
}
