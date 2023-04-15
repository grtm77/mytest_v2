package top.bultrail.markroad.pojo;
import java.util.HashMap;

public class Coordinates {
    private double[][] matrix;
    private HashMap<Integer, String> Hsc1;
    public Coordinates(double[][] matrix,HashMap<Integer, String> Hsc1) {
        this.matrix = matrix;
        this.Hsc1 = Hsc1;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public HashMap<Integer, String> getHsc1() {
        return Hsc1;
    }
}
