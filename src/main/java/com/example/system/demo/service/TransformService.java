package com.example.system.demo.service;

import com.example.system.demo.calculate.CountSet;
import com.example.system.demo.calculate.DBRelation;
import com.example.system.demo.calculate.FileRelation;
import com.example.system.demo.calculate.PythonCal;
import com.example.system.demo.config.RelatedProperties;
import com.example.system.demo.pojo.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
public class TransformService {
    @Autowired
    RelatedProperties relatedProperties;

    @Autowired
    CountSet countSet;

    @Autowired
    CountSet countSetCm;

    @Autowired
    FileRelation fileRelation;

    @Autowired
    DBRelation dbRelation;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    PythonCal pythonCal;

    //保存文件数据到数据库
    public void saveDB(Point points) {
        String roadName = points.getRoadName();
        String pointType = points.getPointType();
        String[][] points1 = points.getPoints();
        List<String[]> strings = Arrays.asList(points1);
        dbRelation.write_new(roadName, strings, pointType);
    }

    //清空数据库
    public void clearDB() {
        dbRelation.clear();
    }

    //生产python脚本计算所需的数据文件
    public void createListData(String flag) throws Exception {
        List<String> all_gateway = collectGatewayFile();
        List<String> all_sensor = collectSensorFile();
        countSet.createPyData(all_gateway,all_sensor,flag);
    }

    //生产python脚本计算所需的数据文件 摄像头
    public void createListDataCm() throws Exception {
        List<String> all_gateway = collectGatewayFile();
        String[] height = relatedProperties.getGatewayHeight().split(",");
        List<String> all_gateway2 = new ArrayList<>();
        for (String s : all_gateway){
            String[] stringArray = s.split(",");
            for(String d : height){
                all_gateway2.add(stringArray[0]+"_"+Double.parseDouble(d)+","+stringArray[1]+","+stringArray[2]);
            }
        }

        List<String> all_sensor = collectSensorFile();
        countSetCm.createPyDataCm(all_gateway2,all_sensor);
    }
    //使用python脚本计算 摄像头
    public HashMap<String,List<List<String>>> calByPythonCm() throws Exception {
        createListDataCm();
        List<String> all_gateway = collectGatewayFile();

        String[] height = relatedProperties.getGatewayHeight().split(",");
        List<String> all_gateway2 = new ArrayList<>();
        for (String s : all_gateway){
            String[] stringArray = s.split(",");
            for(String d : height){
                all_gateway2.add(stringArray[0]+"_"+Double.parseDouble(d)+","+stringArray[1]+","+stringArray[2]);
            }
        }
        List<String> strings = pythonCal.calByPython();

        HashMap<String, List<List<String>>> hs = new HashMap<>();
        ArrayList<String> resultWithCoordinate = new ArrayList<>();

        // 把结果对应的坐标数据返回
        for (String s : all_gateway2) {
            String[] split = s.split(",");
            if (strings.contains(split[0])) {
                String[] tmp = split[0].split("_");
                String data = tmp[0]+ "," +split[1] + "," + split[2]+ "," + tmp[1];
                resultWithCoordinate.add(data);
            }
        }
        //获取所有传感器的数据放入map，一起返回
        List<List<String>> all_sensor02 = collectSensorFile02();
        List<List<String>> all_gateway02 = new ArrayList<>();
        List<List<String>> all_gatewayAll = collectGatewayFile02();
        all_gateway02.add((resultWithCoordinate));
        hs.put("sensorList",all_sensor02);
        hs.put("gatewayList",all_gateway02);
        hs.put("allGatewayList",all_gatewayAll);
        return hs;
    }

    //用贪心算法计算，数据保存在txt中  摄像头
    public  HashMap<String,List<List<String>>> calByGreedyCm() throws Exception {
        List<String> all_sensor = collectSensorFile();
        List<String> all_gateway = collectGatewayFile();

        String[] height = relatedProperties.getGatewayHeight().split(",");

        List<String> all_gateway2 = new ArrayList<>();
        for (String s : all_gateway){
            String[] stringArray = s.split(",");
            for(String d : height){
                all_gateway2.add(stringArray[0]+"_"+Double.parseDouble(d)+","+stringArray[1]+","+stringArray[2]);
            }
        }

        HashMap<String, List<List<String>>> hs = new HashMap<>();

        // 计算结果
        ArrayList<String> resultWithName = null;

        try {
            resultWithName = countSetCm.test01Cm(all_gateway2, all_sensor);
        } catch (Exception e) {
            //System.out.println("错误信息："+e.getMessage());
            throw e;
        }
        ArrayList<String> resultWithCoordinate = new ArrayList<>();

        // 把结果对应的坐标数据返回
        for (String s : all_gateway2) {
            String[] split = s.split(",");
            if (resultWithName.contains(split[0])) {
                String[] tmp = split[0].split("_");
                String data = tmp[0]+ "," +split[1] + "," + split[2]+ "," + tmp[1];
                resultWithCoordinate.add(data);
            }
        }
        //获取所有传感器的数据放入map，一起返回
        List<List<String>> all_sensor02 = collectSensorFile02();
        List<List<String>> all_gatewayAll = collectGatewayFile02();
        List<List<String>> all_gateway02 = new ArrayList<>();
        all_gateway02.add((resultWithCoordinate));
        hs.put("sensorList",all_sensor02);
        hs.put("gatewayList",all_gateway02);
        hs.put("allGatewayList",all_gatewayAll);
        return hs;
    }

    //使用python脚本计算
    public HashMap<String,List<List<String>>> calByPython_upload(String flag) throws Exception {
        createListData(flag);
        List<String> all_sensor = collectSensorFile();
        List<String> all_gateway = collectGatewayFile();
        List<String> strings = pythonCal.calByPython();
        //todo
        HashMap<String, List<List<String>>> hs = new HashMap<>();

        ArrayList<String> resultWithCoordinate = new ArrayList<>();
        ArrayList<String> resultTmp = new ArrayList<>();
        //strings.forEach(System.out::println);
        //all_gateway.forEach(System.out::println);
        // 把结果对应的坐标数据返回
        for (String s : all_gateway) {
            String[] split = s.split(",");
            if (strings.contains(split[0])) {
                String data = split[1] + "," + split[2];
                resultWithCoordinate.add(data);
                resultTmp.add(s);
            }
        }

        //获取所有传感器的数据放入map，一起返回
        List<List<String>> all_sensor02 = collectSensorFile02();
        List<List<String>> all_gateway02 = new ArrayList<>();
        all_gateway02.add((resultWithCoordinate));
        hs.put("sensorList",all_sensor02);
        hs.put("gatewayList",all_gateway02);

        //生成分析结果
//        dbRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

    //用贪心算法计算，数据保存在txt中
//    public List<String> calByGreedy() throws Exception {
//        List<String> all_sensor = collectSensorFile();
//        List<String> all_gateway = collectGatewayFile();
//
//        // 计算结果
//        ArrayList<String> resultWithName = null;
//
//        try {
//            resultWithName = countSet.test01(all_gateway, all_sensor);
//        } catch (Exception e) {
//            //System.out.println("错误信息："+e.getMessage());
//            throw e;
//        }
//
//        ArrayList<String> resultWithCoordinate = new ArrayList<>();
//        //strings.forEach(System.out::println);
//        //all_gateway.forEach(System.out::println);
//        // 把结果对应的坐标数据返回
//        for (String s : all_gateway) {
//            String[] split = s.split(",");
//            if (resultWithName.contains(split[0])) {
//                String data = split[1] + "," + split[2];
//                resultWithCoordinate.add(data);
//            }
//        }
//
//        return resultWithCoordinate;
//    }


    /**
     * 用贪心算法计算，数据保存在txt中,计算上传的文件数据相关 把传感器与网关的信息存入map返回
     * @return
     * @throws Exception
     */
    public HashMap<String,List<List<String>>> calByGreedy_upload(String flag) throws Exception {

        List<List<String>> sensor = dbRelation.readSensor();
        List<List<String>> gateway = dbRelation.readGateway();
        List<String> all_sensor = new ArrayList<String>();
        List<String> all_gateway = new ArrayList<String>();
        HashMap<String, List<List<String>>> hs = new HashMap<>();
        Iterator<List<String>> it1 = sensor.iterator();
        Iterator<List<String>> it2 = gateway.iterator();

        while (it2.hasNext()) {
            List<String> it2next = it2.next();
            String temp2 = it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }

        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果
        ArrayList<String> resultWithName = null;
        try {
            resultWithName = countSet.test01(all_gateway, all_sensor,flag);
        } catch (Exception e) {
            System.out.println("错误信息："+e.getMessage());
            throw e;
        }

        ArrayList<String> resultWithCoordinate = new ArrayList<>();
        ArrayList<String> resultTmp = new ArrayList<>();
        for (String s : all_gateway) {
            String[] split = s.split(",");
            if (resultWithName.contains(split[0])) {
                String data = split[1] + "," + split[2];
                resultWithCoordinate.add(data);
                resultTmp.add(s);
            }
        }

        //获取所有传感器的数据放入map，一起返回
        List<List<String>> all_sensor02 = sensor;
        List<List<String>> all_gateway02 = new ArrayList<>();
        all_gateway02.add((resultWithCoordinate));
        hs.put("sensorList",all_sensor02);
        hs.put("gatewayList",all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

    // 收集gateWay数据，即把各个街道数据文件中数据，整合到一个集合中
    public List<String> collectGatewayFile() throws Exception {
        String str_gateway = relatedProperties.getGatewayPath();
        List<String> all_gateway = new ArrayList<>();
        List<String> gateway = null;

        try {
            // 遍历gateway文件夹下所有文件，添加到gateway集合
            gateway = fileRelation.traverseFolder2(str_gateway);
        } catch (Exception e) {
            throw  e;
        }

        // 把gateway集合中所有文件中的坐标数据放到一个文件
        for (String s : gateway) {
            List<String> fileContent = fileRelation.getFileContent(s);
            all_gateway.addAll(fileContent);
        }
        return all_gateway;
    }

    // 收集sensor数据，即把各个街道数据文件中数据，整合到一个集合中
    public List<String> collectSensorFile() throws Exception {
        String str_sensor = relatedProperties.getSensorPath();
        List<String> all_sensor = new ArrayList<>();
        List<String> sensor = null;

        try {
            // 遍历sensor文件夹下所有文件，添加到sensor集合
            sensor = fileRelation.traverseFolder2(str_sensor);
        } catch (Exception e) {
            throw  e;
        }
        // 把sensor集合中所有文件中的坐标数据放到一个文件
        for (String s : sensor) {
            List<String> fileContent = fileRelation.getFileContent(s);
            all_sensor.addAll(fileContent);
        }
        return all_sensor;
    }

    // 收集crossing数据
    public List<String> collectCrossingFile() {
        String str_crossing = relatedProperties.getCrossingPath();
        List<String> crossing = null;
        try {
            crossing = fileRelation.getFileContent(str_crossing+"\\crosRoad.txt");
        } catch (Exception e) {
            throw  e;
        }
        return crossing;
    }

    // 收集sensor数据，与上传文件相关的计算相关
    public List<List<String>> collectSensorFile02() throws Exception {
        String str_sensor = relatedProperties.getSensorPath();
        List<List<String>> all_sensor = new ArrayList<>();
        List<String> sensor = null;

        try {
            // 遍历sensor文件夹下所有文件，添加到sensor集合
            sensor = fileRelation.traverseFolder2(str_sensor);
        } catch (Exception e) {
            throw  e;
        }
        // 把sensor集合中所有文件中的坐标数据放到一个文件
        for (String s : sensor) {
            List<String> fileContent = fileRelation.getFileContent(s);
            all_sensor.add(fileContent);
        }
        return all_sensor;
    }
    // 收集gateway数据，与上传文件相关的计算相关
    public List<List<String>> collectGatewayFile02() throws Exception {
        String str_gateway = relatedProperties.getGatewayPath();
        List<List<String>> all_gateway = new ArrayList<>();
        List<String> gateway = null;

        try {
            // 遍历sensor文件夹下所有文件，添加到sensor集合
            gateway = fileRelation.traverseFolder2(str_gateway);
        } catch (Exception e) {
            throw  e;
        }
        // 把sensor集合中所有文件中的坐标数据放到一个文件
        for (String s : gateway) {
            List<String> fileContent = fileRelation.getFileContent(s);
            all_gateway.add(fileContent);
        }
        return all_gateway;
    }
    //格式化文件夹,删除目录下的txt文件
    public void deleteFile() {
        //输入要删除文件目录的绝对路径
        File file1 = new File(relatedProperties.getSensorPath());
        File file2 = new File(relatedProperties.getGatewayPath());
        File file3 = new File(relatedProperties.getCrossingPath());
        fileRelation.deleteFile(file1);
        fileRelation.deleteFile(file2);
        fileRelation.deleteFile(file3);
    }

    //用redis保存
//    public void saveByRedis(Point points) throws IOException {
//        String roadName = points.getRoadName();
//        String pointType = points.getPointType();
//        String[] points1 = points.getPoints();
//        List<String> strings = Arrays.asList(points1);
//
//        ListOperations<String, String> listOperations = redisTemplate.opsForList();
//
//        try {
//             fileRelation.saveDataByRedis(roadName, strings, pointType);
//        } catch (IOException e) {
//            throw e;
//        }
//    }

    //用贪心算法计算，数据保存在redis中
//    public List<String> calByGreedy02() throws Exception {
//        ListOperations<String, String> listOperations = redisTemplate.opsForList();
//        //从redis中取出数据
//        List<String> all_sensor = listOperations.range("sensor", 0, -1);
//        List<String> all_gateway = listOperations.range("gateway", 0, -1);
//
//        // 计算结果
//        ArrayList<String> resultWithName = null;
//
//        try {
//            if(all_gateway.size()==0 || all_sensor.size()==0)throw new Exception("没有数据！");
//            resultWithName = countSet.test01(all_gateway, all_sensor);
//        } catch (Exception e) {
//            //System.out.println("错误信息："+e.getMessage());
//            throw e;
//        }
//
//        ArrayList<String> resultWithCoordinate = new ArrayList<>();
//        // 把结果对应的坐标数据返回
//        for (String s : all_gateway) {
//            String[] split = s.split(",");
//            if (resultWithName.contains(split[0])) {
//                String data = split[1] + "," + split[2];
//                resultWithCoordinate.add(data);
//            }
//        }
//        return resultWithCoordinate;
//    }

    //删除redis中的数据
//    public void clearRedisData() {
//        ListOperations<String, String> listOperations = redisTemplate.opsForList();
//        while(listOperations.size("sensor")!=0){
//            listOperations.leftPop("sensor");
//        }
//        while(listOperations.size("gateway")!=0){
//            listOperations.leftPop("gateway");
//        }
//    }


}