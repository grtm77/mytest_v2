package top.bultrail.markroad.pojo;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MatrixResult {
    private double[][] matrix;
    private BiMap<Integer, Integer> hash_col2gatewayIndex;
    private BiMap<Integer, Integer> hash_row2sensorIndex;

    public MatrixResult(double[][] matrix, BiMap<Integer, Integer> hash_col2gatewayIndex, BiMap<Integer, Integer> hash_row2sensorIndex) {
        this.matrix = matrix;
        this.hash_col2gatewayIndex = hash_col2gatewayIndex;
        this.hash_row2sensorIndex = hash_row2sensorIndex;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public BiMap<Integer, Integer> getHash_col2gatewayIndex() {
        return hash_col2gatewayIndex;
    }

    public BiMap<Integer, Integer> getHash_row2sensorIndex() {
        return hash_row2sensorIndex;
    }
}

