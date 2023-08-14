package top.bultrail.markroad.util;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLDouble;

import java.io.IOException;
import java.util.ArrayList;

public class MatrixToMatFile {
    public static void main(String[] args) {
        // 示例数据
        double[][] matrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };

        // 使用JMatio将double[][]转换为MLDouble
        MLDouble mlMatrix = new MLDouble("Coverage", matrix);

        // 创建一个包含MLDouble的ArrayList并保存为.mat文件
        ArrayList list = new ArrayList();
        list.add(mlMatrix);

        // 调用自定义方法，传递double[][]和文件名
        saveDoubleMatrixToMatFile(matrix, "RN#test.mat");
    }

    // 添加一个方法，接收double[][]和文件名作为参数，生成.mat文件
    public static void saveDoubleMatrixToMatFile(double[][] matrix, String fileName) {
        MLDouble mlMatrix = new MLDouble("Coverage", matrix);

        ArrayList list = new ArrayList();
        list.add(mlMatrix);

        try {
            new MatFileWriter(fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
