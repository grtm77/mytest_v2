package top.bultrail.markroad.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuickSave implements Serializable {

//    private double[][] cross_points;
//    private double[][][] gateway_array;
//    private double[][][] sensor_array;
    private String[][] cross_points;
    private String[][][] gateway_array;
    private String[][][] sensor_array;


}


