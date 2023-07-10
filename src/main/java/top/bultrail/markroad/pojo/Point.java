package top.bultrail.markroad.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point implements Serializable {

    private String roadName;
    private String[][] points;
    private String pointType;

}


