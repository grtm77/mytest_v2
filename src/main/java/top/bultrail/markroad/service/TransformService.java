package top.bultrail.markroad.service;

import top.bultrail.markroad.calculate.CountSet;
import top.bultrail.markroad.calculate.DBRelation;
import top.bultrail.markroad.calculate.FileRelation;
import top.bultrail.markroad.config.RelatedProperties;
import top.bultrail.markroad.pojo.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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


    //保存文件数据到数据库 新增
    public void saveDB(Point points) {
        String roadName = points.getRoadName();
        String pointType = points.getPointType();
        String[][] points1 = points.getPoints();
        List<String[]> strings = Arrays.asList(points1);
        dbRelation.write_new(roadName, strings, pointType);
    }

    //清空数据库 新增
    public void clearDB() {
        dbRelation.clear();
    }

    //排序 新增
    public void sortDB() {
        dbRelation.sort();
    }

    //备份 新增
    public void bkDB() {
        dbRelation.bkup();
    }

    //162 新增
    public void td232() {
        dbRelation.clear();
        dbRelation.tdata("232");
    }

    //321 新增
    public void td466() {
        dbRelation.clear();
        dbRelation.tdata("466");
    }

    //518 新增
    public void td1056() {
        dbRelation.clear();
        dbRelation.tdata("1056");
    }

    public void td1500() {
        dbRelation.clear();
        dbRelation.tdata("1500");
    }

    public void tdGuo(int num) {
        dbRelation.clear();
        dbRelation.tdataG(num + "");
    }

    //还原 新增
    public void resDB() {
        dbRelation.clear();
        dbRelation.res();
    }


    //使用线性规划算法计算
    public HashMap<String,List<List<String>>> calByPython_upload(String flag) throws Exception {
        List<List<String>> sensor = dbRelation.readSensor();
        List<List<String>> gateway = dbRelation.readGateway();
        List<String> all_sensor = new ArrayList<String>();
        List<String> all_gateway = new ArrayList<String>();
        HashMap<String, List<List<String>>> hs = new HashMap<>();
        Iterator<List<String>> it1 = sensor.iterator();
        Iterator<List<String>> it2 = gateway.iterator();

        while (it2.hasNext()) {
            List<String> it2next = it2.next();
            String temp2 = it2next.get(0) + it2next.get(1) + "," + it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }

        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(0) + it1next.get(1) + "," +  it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果
        ArrayList<String> resultWithName = null;

        resultWithName = countSet.calByMatLP(all_gateway, all_sensor, flag);


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
        hs.put("sensorList", all_sensor02);
        hs.put("gatewayList", all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

    //使用遗传计算
    public HashMap<String,List<List<String>>> calByGA_upload(String flag) throws Exception {
        //从数据库读取的sensor与gateway集
        List<List<String>> sensor = dbRelation.readSensor();
        List<List<String>> gateway = dbRelation.readGateway();
        // 转化数据库中读取的结果
        List<String> all_sensor = new ArrayList<String>();
        List<String> all_gateway = new ArrayList<String>();
        HashMap<String, List<List<String>>> hs = new HashMap<>();

        Iterator<List<String>> it1 = sensor.iterator();
        Iterator<List<String>> it2 = gateway.iterator();
        // 网关节点集
        while (it2.hasNext()) {
            List<String> it2next = it2.next();
            String temp2 = it2next.get(0) + it2next.get(1) + "," + it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }
        // 传感器节点集
        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(0) + it1next.get(1) + "," +  it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果  返回被选中的网关节点名
        ArrayList<String> resultWithName = null;
        resultWithName = countSet.test06(all_gateway, all_sensor, flag);

        // 存储网关集的位置信息
        ArrayList<String> resultWithCoordinate = new ArrayList<>();
        // 存储网关整体信息
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
        hs.put("sensorList", all_sensor02);
        hs.put("gatewayList", all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

    /**
     * 用贪心算法计算，数据保存在txt中,计算上传的文件数据相关 把传感器与网关的信息存入map返回 实现
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
            String temp2 = it2next.get(0) + it2next.get(1) + "," + it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }

        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(0) + it1next.get(1) + "," +  it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果
        ArrayList<String> resultWithName = null;
        try {
            resultWithName = countSet.test01(all_gateway, all_sensor, flag);
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
        hs.put("sensorList", all_sensor02);
        hs.put("gatewayList", all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

    /**
     * 用有向贪心计算，数据保存在txt中,计算上传的文件数据相关 把传感器与网关的信息存入map返回 新增
     * @return
     * @throws Exception
     */
    public HashMap<String,List<List<String>>> calByLinner_upload(String flag) throws Exception {

        List<List<String>> sensor = dbRelation.readSensor();
        List<List<String>> gateway = dbRelation.readGateway();
        List<String> all_sensor = new ArrayList<String>();
        List<String> all_gateway = new ArrayList<String>();
        HashMap<String, List<List<String>>> hs = new HashMap<>();
        Iterator<List<String>> it1 = sensor.iterator();
        Iterator<List<String>> it2 = gateway.iterator();

        while (it2.hasNext()) {
            List<String> it2next = it2.next();
            String temp2 = it2next.get(0) + it2next.get(1) + "," + it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }

        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(0) + it1next.get(1) + "," +  it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果
        ArrayList<String> resultWithName = null;
//        try {
            resultWithName = countSet.test03(all_gateway, all_sensor, flag);
//        } catch (Exception e) {
//            System.out.println("错误信息："+e.getMessage());
//            throw e;
//        }

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
        hs.put("sensorList", all_sensor02);
        hs.put("gatewayList", all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

    /**
     * 用分支限界计算，数据保存在txt中,计算上传的文件数据相关 把传感器与网关的信息存入map返回
     * @return
     * @throws Exception
     */
    public HashMap<String,List<List<String>>> calByBB(String flag) throws Exception {
        List<List<String>> sensor = dbRelation.readSensor();
        List<List<String>> gateway = dbRelation.readGateway();
        List<String> all_sensor = new ArrayList<String>();
        List<String> all_gateway = new ArrayList<String>();
        HashMap<String, List<List<String>>> hs = new HashMap<>();
        Iterator<List<String>> it1 = sensor.iterator();
        Iterator<List<String>> it2 = gateway.iterator();

        while (it2.hasNext()) {
            List<String> it2next = it2.next();
            String temp2 = it2next.get(0) + it2next.get(1) + "," + it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }

        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(0) + it1next.get(1) + "," +  it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果
        ArrayList<String> resultWithName = null;
//        try {
        resultWithName = countSet.calByBB(all_gateway, all_sensor, flag);
//        } catch (Exception e) {
//            System.out.println("错误信息："+e.getMessage());
//            throw e;
//        }

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
        hs.put("sensorList", all_sensor02);
        hs.put("gatewayList", all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

    /**
     * 用matlab线性规划计算，数据保存在txt中,计算上传的文件数据相关 把传感器与网关的信息存入map返回
     * @return
     * @throws Exception
     */
    public HashMap<String,List<List<String>>> calByLP(String flag) throws Exception {

        List<List<String>> sensor = dbRelation.readSensor();
        List<List<String>> gateway = dbRelation.readGateway();
        List<String> all_sensor = new ArrayList<String>();
        List<String> all_gateway = new ArrayList<String>();
        HashMap<String, List<List<String>>> hs = new HashMap<>();
        Iterator<List<String>> it1 = sensor.iterator();
        Iterator<List<String>> it2 = gateway.iterator();

        while (it2.hasNext()) {
            List<String> it2next = it2.next();
            String temp2 = it2next.get(0) + it2next.get(1) + "," + it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }

        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(0) + it1next.get(1) + "," +  it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果
        ArrayList<String> resultWithName = null;
//        try {
        // ortools 线性规划
        resultWithName = countSet.calByorLP(all_gateway, all_sensor, flag);
        // matlab 线性规划
//        resultWithName = countSet.calByMatLP(all_gateway, all_sensor, flag);
//        } catch (Exception e) {
//            System.out.println("错误信息："+e.getMessage());
//            throw e;
//        }

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
        hs.put("sensorList", all_sensor02);
        hs.put("gatewayList", all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }



    /**
     * 用蚁群计算，计算上传的文件数据相关 把传感器与网关的信息存入map返回 新增
     * @return
     * @throws Exception
     */
    public HashMap<String,List<List<String>>> calByAco_upload(String flag) throws Exception {

        List<List<String>> sensor = dbRelation.readSensor();
        List<List<String>> gateway = dbRelation.readGateway();
        List<String> all_sensor = new ArrayList<String>();
        List<String> all_gateway = new ArrayList<String>();
        HashMap<String, List<List<String>>> hs = new HashMap<>();
        Iterator<List<String>> it1 = sensor.iterator();
        Iterator<List<String>> it2 = gateway.iterator();

        while (it2.hasNext()) {
            List<String> it2next = it2.next();
            String temp2 = it2next.get(0) + it2next.get(1) + "," + it2next.get(2) + "," + it2next.get(3);
            all_gateway.add(temp2);
        }

        while (it1.hasNext()) {
            List<String> it1next = it1.next();
            String temp1 = it1next.get(0) + it1next.get(1) + "," +  it1next.get(2) + "," + it1next.get(3);
            all_sensor.add(temp1);
        }

        // 计算结果
        ArrayList<String> resultWithName = null;

        resultWithName = countSet.test04(all_gateway, all_sensor, flag);


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
        hs.put("sensorList", all_sensor02);
        hs.put("gatewayList", all_gateway02);

        //生成分析结果
        fileRelation.creatResult(all_sensor,resultTmp);
        return hs;
    }

}