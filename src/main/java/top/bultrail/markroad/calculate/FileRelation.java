package top.bultrail.markroad.calculate;


import top.bultrail.markroad.config.RelatedProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

//更换文本格式的脚本代码
@Component
public class FileRelation {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RelatedProperties relatedProperties;

    @Autowired
    CountSet countSet;


//    废弃
    public List<String> traverseFolder2(String path) throws Exception {
        List<String> ls = new LinkedList<>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                throw new Exception("还没有添加任何文件！");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
//                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                        //System.out.println("文件:" + file2.getAbsolutePath());
                        ls.add(file2.getAbsolutePath());
                    }
                }
            }
        } else {
            throw new Exception("文件不存在");
        }
        return ls;
    }

    /**
     * 获取文件内容 废弃
     *
     * @param path
     * @return
     */
    public  List<String> getFileContent(String path) {
        List<String> strList2 = new ArrayList<>();
        try {
            File file = new File(path);
            InputStreamReader read = new InputStreamReader(new FileInputStream(
                    file), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                strList2.add(line);
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strList2;
    }

    /**0.00005913
     * 计算各个坐标与中心点的距离，从而算出路口的点 实现
     * @param b1_longitude   坐标1 经度
     * @param b1_laitude     坐标1 纬度
     * @param b2_longitude   坐标2 经度
     * @param b2_laitude     坐标2 纬度
     * @return
     */
    public  double c_distance(double b1_longitude ,double b1_laitude,double b2_longitude ,double b2_laitude ){

        BigDecimal bigDecimal_1_x =new BigDecimal(Double.valueOf(b1_longitude));
        BigDecimal bigDecimal_1_y =new BigDecimal(Double.valueOf( b1_laitude));
        BigDecimal bigDecimal_2_x =new BigDecimal(Double.valueOf(b2_longitude));
        BigDecimal bigDecimal_2_y =new BigDecimal(Double.valueOf( b2_laitude));

        // 计算网关与节点之间的距离
        BigDecimal subtract_x = bigDecimal_1_x.subtract(bigDecimal_2_x);
        BigDecimal subtract_y = bigDecimal_1_y.subtract(bigDecimal_2_y);
        BigDecimal multiply_x = subtract_x.multiply(subtract_x);
        BigDecimal multiply_y = subtract_y.multiply(subtract_y);
        BigDecimal add = multiply_x.add(multiply_y);

        //使用牛顿迭代法计算平方根
        BigDecimal sqrt = new CountSet().sqrt(add);
        double distance = sqrt.doubleValue();

        return distance;
    }

    /**
     * 计算路口的情况下，生成结果，传感器被哪些网关覆盖，网关覆盖了哪些传感器 实现
     * @param sensorListResult list里面String的格式: "名字,经度，维度 "，网关list格式相同
     * @param gatewayListResult
     */
    public void creatResult(List<String> sensorListResult,List<String> gatewayListResult){

        double radius = relatedProperties.getGatewayRadius();

        HashMap<Integer,Integer> sensorAnalysis = new HashMap<>();
        HashMap<Integer,Integer> gatewayAnalysis = new HashMap<>();
        Integer sensorCount = 0;
        Integer gatewayCount = 0;

        //路口数据集合
        HashMap<String, Vector<Double>> crosMap = countSet.countCrossing();

        FileWriter fileWriter1 = null;
        try {
            fileWriter1 = new FileWriter(new File(relatedProperties.getResultPath()+"/sensorResult.txt"));
            fileWriter1.write("传感器节点文件格式： 传感器节点名称 -> 能够覆盖的该传感器节点的网关名称 "+"\n");
            for(String sensor : sensorListResult){
                String[] sensorSplit = sensor.split(",");
                StringBuilder sensor_tmp = new StringBuilder(sensorSplit[0]+" ->");
                for(String gateway : gatewayListResult){
                    String[] gatewaySplit = gateway.split(",");
                    double temp_d = c_distance(Double.parseDouble(sensorSplit[1]), Double.parseDouble(sensorSplit[2]), Double.parseDouble(gatewaySplit[1]), Double.parseDouble(gatewaySplit[2]));
                    if(radius > temp_d  ){
                         if(sensorSplit[0].substring(0,3).equals(gatewaySplit[0].substring(0,3))){
                             sensor_tmp.append("  "+gatewaySplit[0]);
                             sensorCount++;
                         }else{
                             for (Map.Entry<String, Vector<Double>> entry_cros : crosMap.entrySet()) {
                                 Vector<Double> cros_vc = entry_cros.getValue();
                                 double x_cros_vc = cros_vc.get(0);
                                 double y_cros_vc = cros_vc.get(1);
                                 //判断每个传感器与网关是否在一个路口
                                 double d_gateway = c_distance(x_cros_vc, y_cros_vc, Double.parseDouble(gatewaySplit[1]), Double.parseDouble(gatewaySplit[2]));
                                 double d_sensor = c_distance(x_cros_vc, y_cros_vc, Double.parseDouble(sensorSplit[1]), Double.parseDouble(sensorSplit[2]));
                                 if (d_gateway < relatedProperties.getCrossingRadius() && d_sensor < relatedProperties.getCrossingRadius()) {
                                     //如果在一个路口则把传感器加入网关集合。
                                     sensor_tmp.append("  "+gatewaySplit[0]);
                                     sensorCount++;
                                 }
                             }
                         }
                    }
                }
                sensorAnalysis.put(sensorCount,sensorAnalysis.getOrDefault(sensorCount,0)+1);
                sensorCount = 0;
                fileWriter1.write(sensor_tmp+"\r\n");
                fileWriter1.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileWriter1!=null){
                try {
                    fileWriter1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FileWriter fileWriter2 = null;
        try {
            fileWriter2 = new FileWriter(new File(relatedProperties.getResultPath()+"/gatewayResult.txt"));
            fileWriter2.write("网关节点文件格式： 网关节点名称 -> 被该网关覆盖的传感器节点名称 "+"\r\n");
            for(String gateway : gatewayListResult){
                String[] gatewaySplit = gateway.split(",");
                StringBuilder gateway_tmp = new StringBuilder(gatewaySplit[0]+" ->");
                for(String sensor : sensorListResult){
                    String[] sensorSplit = sensor.split(",");
                    double temp_d = c_distance(Double.parseDouble(sensorSplit[1]), Double.parseDouble(sensorSplit[2]), Double.parseDouble(gatewaySplit[1]), Double.parseDouble(gatewaySplit[2]));
                    if(radius > temp_d  ){
                        if(sensorSplit[0].substring(0,3).equals(gatewaySplit[0].substring(0,3))){
                            gateway_tmp.append("  "+sensorSplit[0]);
                            gatewayCount++;
                        }else{
                            for (Map.Entry<String, Vector<Double>> entry_cros : crosMap.entrySet()) {
                                Vector<Double> cros_vc = entry_cros.getValue();
                                double x_cros_vc = cros_vc.get(0);
                                double y_cros_vc = cros_vc.get(1);
                                //判断每个传感器与网关是否在一个路口
                                double d_gateway = c_distance(x_cros_vc, y_cros_vc, Double.parseDouble(gatewaySplit[1]), Double.parseDouble(gatewaySplit[2]));
                                double d_sensor = c_distance(x_cros_vc, y_cros_vc, Double.parseDouble(sensorSplit[1]), Double.parseDouble(sensorSplit[2]));
                                if (d_gateway < relatedProperties.getCrossingRadius() && d_sensor < relatedProperties.getCrossingRadius()) {
                                    //如果在一个路口则把传感器加入网关集合。
                                    gateway_tmp.append("  "+sensorSplit[0]);
                                    gatewayCount++;
                                }
                            }
                        }
                    }
                }
                gatewayAnalysis.put(gatewayCount,gatewayAnalysis.getOrDefault(gatewayCount,0)+1);
                gatewayCount=0;
                fileWriter2.write(gateway_tmp+"\r\n");
                fileWriter2.write("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileWriter2!=null){
                try {
                    fileWriter2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FileWriter fileWriter3 = null;
        try {
            fileWriter3 = new FileWriter(new File(relatedProperties.getResultPath()+"/resultAnalysis.txt"));
            fileWriter3.write("<--传感器节点分析-->"+"\r\n");

            for(Map.Entry<Integer,Integer> entry : sensorAnalysis.entrySet()){
                fileWriter3.write("有 "+entry.getValue()+" 个传感器节点被覆盖了 "+entry.getKey()+" 次！"+"\r\n");
            }

            fileWriter3.write("\r\n");
            fileWriter3.write("\r\n");
            fileWriter3.write("<--网关节点分析-->"+"\r\n");
            int countGateway = 0;
            for(Map.Entry<Integer,Integer> entry : gatewayAnalysis.entrySet()){
                fileWriter3.write("有 "+entry.getValue()+" 个网关覆盖了 "+entry.getKey()+" 个传感器节点！"+"\r\n");
                countGateway+= entry.getValue();
            }
            fileWriter3.write("网关总数为："+countGateway);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileWriter3!=null){
                try {
                    fileWriter3.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}