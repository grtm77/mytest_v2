package top.bultrail.markroad.calculate;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import top.bultrail.markroad.config.RelatedProperties;
import eu.andredick.aco.algorithm.AbstractAlgorithm;
import eu.andredick.aco.algorithm.Statistics;
import eu.andredick.configuration.AlgorithmConfiguration_self;
import eu.andredick.scp.SCPSolution;
import eu.andredick.scp.SCProblem;
import eu.andredick.tools.MatConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.bultrail.markroad.pojo.Coordinates;
import top.bultrail.markroad.util.doubleToBool;

import java.beans.PropertyVetoException;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.util.*;

@Component
public class CountSet {
    @Autowired
    RelatedProperties relatedProperties;

    @Autowired
    FileRelation fileRelation;

    @Autowired
    DBRelation dbRelation;

    /**
     * 从目标文件中读取灯柱和节点坐标
     * @param path
     * @return
     */
    public List<String> getFileContent(String path) {

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


    /**
     * 从数据库中读取灯柱和节点坐标
     * @return
     */
    public List<List<String>> getDBContent() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        Connection conn;
        Statement stmt = null;
        try {
            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
            cpds.setJdbcUrl("jdbc:mysql://10.45.39.13:3306/Marks");
            cpds.setUser("mytest");
            cpds.setPassword("12345678");
            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(10);
            conn = cpds.getConnection();
            stmt = conn.createStatement();
        } catch (SQLException | PropertyVetoException e) {
            e.printStackTrace();
        }
        String tsql = "SELECT * FROM crossing";
        ResultSet rs = null;
        List<String> lis;
        List<List<String>> bk = new ArrayList();
        try {
            rs = stmt.executeQuery(tsql);
            while (rs.next()) {
                lis = new ArrayList();
                lis.add(rs.getString("roadName"));
                lis.add(rs.getString("numberInRoad"));
                lis.add(rs.getString("Lng"));
                lis.add(rs.getString("Lat"));
                bk.add(lis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bk;
    }

    /**
     * 将每个路口的信息存入map
     *
     * @return
     */
    public HashMap<String, Vector<Double>> countCrossing() {
        List<List<String>> cList = getDBContent();
        List<String> crossingList = new ArrayList<String>();
        Iterator<List<String>> it = cList.iterator();

        while (it.hasNext()) {
            List<String> itnext = it.next();
            String temp2 = itnext.get(2) + "," + itnext.get(3);
            crossingList.add(temp2);
        }

//        将字符串转化为数值向量
        HashMap<String, Vector<Double>> cros = new HashMap<>();

        for (int i = 0; i < crossingList.size(); i++) {
            Vector<Double> vc = new Vector<>();
//            System.out.println(crossingList.get(i));
            String[] split = crossingList.get(i).split(",");
            vc.add(Double.parseDouble(split[0]));
            vc.add(Double.parseDouble(split[1]));
            cros.put(crossingList.get(i), vc);
        }
        return cros;
    }

    /**
     * 计算每个网关能包含的节点集合，包含路口的计算
     *
     * @param gateway   网关集
     * @param sensor    传感器集
     * @param radius    覆盖半径
     * @param cros      路口集合
     * @return  每个网关包含的节点集合
     */
    public Map<String, Map<String, Vector<Double>>> countSet(Map<String, Vector<Double>> gateway, Map<String, Vector<Double>> sensor, Map<String, Vector<Double>> cros, double radius) {

        Map<String, Map<String, Vector<Double>>> coverMap = new HashMap<>();
        for (Map.Entry<String, Vector<Double>> entry_gateway : gateway.entrySet()) {

            // 计算每个网关的x，y坐标(经纬度)
            Vector<Double> gateway_vc = entry_gateway.getValue();
            double x_gateway_vc = gateway_vc.get(0);
            double y_gateway_vc = gateway_vc.get(1);
            // 新建一个map存放每个网关所能覆盖的节点
            Map<String, Vector<Double>> coverMap_gateway = new HashMap<>();


            for (Map.Entry<String, Vector<Double>> entry_sensor : sensor.entrySet()) {

                // 计算每个节点的x，y坐标(经纬度)
                Vector<Double> sensor_vc = entry_sensor.getValue();
                double x_sensor_vc = sensor_vc.get(0);
                double y_sensor_vc = sensor_vc.get(1);
                double distance = 0;

                // 计算网关与节点之间的距离
//                System.out.println(entry_sensor.getKey());
                try {
                    distance = fileRelation.c_distance(x_gateway_vc, y_gateway_vc, x_sensor_vc, y_sensor_vc);
                }catch (Exception e) {
                    System.out.println("报这个错的解决办法：");
                    System.out.println("解除CountSet第157行代码的注释");
                    System.out.println("找到每次报错前打印出的最后一个名字");
                    System.out.println("在sensor表中删除这个sensor");
                    System.out.println("直到所有报错的sensor删完为止，具体错误原因未知");
                    throw new RuntimeException();
                }

                String string_gateway = entry_gateway.getKey().substring(0, 3);
                String string_sensor = entry_sensor.getKey().substring(0, 3);

//                System.out.println(string_gateway==string_sensor);
//                System.out.println(string_gateway.equals(string_sensor));

                // 如果距离在当前网关的覆盖之内，则把当前节点加入到当前网关的map集合
                // 如果传感器节点与网关节点重合，则直接加入，避免计算时发生错误
                if ((distance < radius) && (string_gateway.equals(string_sensor))) {
                    coverMap_gateway.put(entry_sensor.getKey(), entry_sensor.getValue());
                } else if (distance < radius) {
                    for (Map.Entry<String, Vector<Double>> entry_cros : cros.entrySet()) {
                        Vector<Double> cros_vc = entry_cros.getValue();
                        double x_cros_vc = cros_vc.get(0);
                        double y_cros_vc = cros_vc.get(1);
                        //判断每个传感器与网关是否在一个路口
                        double d_gateway = fileRelation.c_distance(x_cros_vc, y_cros_vc, x_gateway_vc, y_gateway_vc);
                        double d_sensor = fileRelation.c_distance(x_cros_vc, y_cros_vc, x_sensor_vc, y_sensor_vc);
                        if (d_gateway < relatedProperties.getCrossingRadius() && d_sensor < relatedProperties.getCrossingRadius()) {
                            //如果在一个路口则把传感器加入网关集合。
                            coverMap_gateway.put(entry_sensor.getKey(), entry_sensor.getValue());
                        }
                    }
                }
            }
            // 把当前网关和它所能覆盖到的节点加入到map集合
            coverMap.put(entry_gateway.getKey(), coverMap_gateway);
        }
//        System.out.println("coverMap = " + coverMap);
        return coverMap;
    }

    /**
     * 计算每个网关能包含的节点集合，不包含路口的计算
     *
     * @param gateway   网关集
     * @param sensor    传感器集
     * @param radius    覆盖半径
     * @return  每个网关包含的节点集合
     */
    public Map<String, Map<String, Vector<Double>>> countSet(Map<String, Vector<Double>> gateway, Map<String, Vector<Double>> sensor,  double radius) {

        Map<String, Map<String, Vector<Double>>> coverMap = new HashMap<>();

        for (Map.Entry<String, Vector<Double>> entry_gateway : gateway.entrySet()) {

            // 计算每个网关的x，y坐标(经纬度)
            Vector<Double> gateway_vc = entry_gateway.getValue();
            double x_gateway_vc = gateway_vc.get(0);
            double y_gateway_vc = gateway_vc.get(1);
            // 新建一个map存放每个网关所能覆盖的节点
            Map<String, Vector<Double>> coverMap_gateway = new HashMap<>();

            for (Map.Entry<String, Vector<Double>> entry_sensor : sensor.entrySet()) {

                // 计算每个节点的x，y坐标(经纬度)
                Vector<Double> sensor_vc = entry_sensor.getValue();
                double x_sensor_vc = sensor_vc.get(0);
                double y_sensor_vc = sensor_vc.get(1);

                // 计算网关与节点之间的距离
                double distance = fileRelation.c_distance(x_gateway_vc, y_gateway_vc, x_sensor_vc, y_sensor_vc);

                String string_gateway = entry_gateway.getKey().substring(0, 3);
                String string_sensor = entry_sensor.getKey().substring(0, 3);

                // 如果距离在当前网关的覆盖之内，且属于同一路段，则把当前节点加入到当前网关的map集合
                if ((distance < radius) && (string_gateway.equals(string_sensor))) {
                    coverMap_gateway.put(entry_sensor.getKey(), entry_sensor.getValue());
                }
            }
            // 把当前网关和它所能覆盖到的节点加入到map集合
            coverMap.put(entry_gateway.getKey(), coverMap_gateway);
        }
//        System.out.println("coverMap = " + coverMap);
        return coverMap;
    }

    /**
     * //使用牛顿迭代法计算平方根_1
     *
     * @param num
     * @return
     */
    public BigDecimal sqrt(BigDecimal num) {
        if (num.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal x = num.divide(new BigDecimal("2"), MathContext.DECIMAL128);
        while (x.subtract(x = sqrtIteration(x, num)).abs().compareTo(new BigDecimal("0.0000000000000000000001")) > 0) ;
        return x;
    }

    /**
     * 使用牛顿迭代法计算平方根_2
     *
     * @param x
     * @param n
     * @return
     */
    private BigDecimal sqrtIteration(BigDecimal x, BigDecimal n) {
        return x.add(n.divide(x, MathContext.DECIMAL128)).divide(new BigDecimal("2"), MathContext.DECIMAL128);
    }

//    /**
//     * 生成python数据
//     *
//     * @param gateWayList
//     * @param sensorList
//     */
//    public void createPyData(List<String> gateWayList, List<String> sensorList,String flag) throws Exception {
//        String property = System.getProperty("user.dir");
//        BufferedWriter bfs = null;
//        BufferedWriter bfg = null;
//        //生成py网关数据
//        try {
//            bfs = new BufferedWriter(new FileWriter(property + "\\src\\main\\java\\com\\example\\system\\demo\\calculate\\gateWayData.txt"));
//            //bfg = new BufferedWriter(new FileWriter("C:\\Users\\THTF\\Desktop\\gateway\\gateWayData.txt"));
//            Map<String, Map<String, Vector<Double>>> result = test02(gateWayList, sensorList,flag);
//            // 灯柱包含的传感器的集合 写入文件
//            for (Map.Entry<String, Map<String, Vector<Double>>> keyValue : result.entrySet()) {
//                Map<String, Vector<Double>> value = keyValue.getValue();
//                bfs.write(keyValue.getKey() + " ");
//                for (String key : value.keySet()) {
//                    bfs.write(key + " ");
//                }
//                bfs.newLine();
//            }
//            bfs.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bfs != null) bfs.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //生成py传感器数据
//        try {
//            bfg = new BufferedWriter(new FileWriter(property + "\\src\\main\\java\\com\\example\\system\\demo\\calculate\\sensorData.txt"));
//            for (String s : sensorList) {
//                String[] split = s.split(",");
//                bfg.write(split[0]);
//                bfg.newLine();
//            }
//            bfg.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bfg != null) bfs.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 通过网关集与传感器集计算覆盖矩阵 及 hsc1
     * @param strList1  网关集合  “DMXLS0,113.807255,22.816363”
     * @param strList2  传感器集合
     * @param flag 是否考虑路口
     */

    public Coordinates getMatrixHsc1(List<String> strList1, List<String> strList2, String flag) throws Exception {
        // 节点名与位置对应
        HashMap<String, Vector<Double>> hs1 = new HashMap<>();
        HashMap<String, Vector<Double>> hs2 = new HashMap<>();
        // 编号与节点名对应
        HashMap<Integer, String> hsc1 = new HashMap<>();
        HashMap<Integer, String> hsc2 = new HashMap<>();
        // 节点名与编号对应
        HashMap<String, Integer> hsc1b = new HashMap<>();
        HashMap<String, Integer> hsc2b = new HashMap<>();
        // 所有的传感器集合
        HashSet<String> hashSet = new HashSet<>();

        for (int i = 0; i < strList1.size(); i++) {
            Vector<Double> vc = new Vector<>();
            String[] split = strList1.get(i).split(",");
            vc.add(Double.parseDouble(split[1]));
            vc.add(Double.parseDouble(split[2]));
            hs1.put(split[0], vc);
            hsc1.put(i, split[0]);
            hsc1b.put(split[0], i);
        }
        for (int i = 0; i < strList2.size(); i++) {
            Vector<Double> vc = new Vector<>();
            String[] split = strList2.get(i).split(",");
            vc.add(Double.parseDouble(split[1]));
            vc.add(Double.parseDouble(split[2]));
            hashSet.add(split[0]);
            hs2.put(split[0], vc);
            hsc2.put(i, split[0]);
            hsc2b.put(split[0], i);
        }

        double raius = relatedProperties.getGatewayRadius();

        Map<String, Map<String, Vector<Double>>> stringMapMap = null;
        if ("withoutCros".equals(flag)) {
            stringMapMap = countSet(hs1, hs2, raius);
        } else {
            // 计算路口集合
            HashMap<String, Vector<Double>> crossingMap = countCrossing();
            stringMapMap = countSet(hs1, hs2, crossingMap, raius);
        }

        // 节点名之间的对应关系
        HashMap<String, HashSet<String>> stringHashSetHashMap = new HashMap<>();

        for (Map.Entry<String, Map<String, Vector<Double>>> entryt : stringMapMap.entrySet()) {
            Map<String, Vector<Double>> valu2e = entryt.getValue();
            HashSet<String> hashss = new HashSet<>();
            for (Map.Entry<String, Vector<Double>> maps : valu2e.entrySet()) {
                String key = maps.getKey();
                hashss.add(key);
            }
            stringHashSetHashMap.put(entryt.getKey(), hashss);
        }

        //计算当前的所有灯柱能否包含所有传感器
        //当前所有网关覆盖的集合
        HashSet<String> resultash = new HashSet<>();
        for (Map.Entry<String, HashSet<String>> entry : stringHashSetHashMap.entrySet()) {
            HashSet<String> value = entry.getValue();
            Iterator<String> iterator1 = value.iterator();
            while (iterator1.hasNext()) {
                resultash.add(iterator1.next());
            }
        }

        // 判断传感器有没有被全部覆盖到
        if (resultash.size() != hashSet.size()) {
            //找出未被覆盖的sensor
            for (String value : hashSet) {
                int f = 0;
                for (String key : resultash) {
                    if (value == key) {
                        f = 1;
                        break;
                    }
                }
                if (f == 0) System.out.println(value);
            }
            throw new Exception("传感器没有被全部覆盖到!");
        }

//        对stringHashSetHashMap操作
        // sensor_size x gateway_size
        double[][] matrix = new double[strList2.size()][strList1.size()];
        for (String key : stringHashSetHashMap.keySet()) {
            for (String i : stringHashSetHashMap.get(key)) {
                matrix[hsc2b.get(i)][hsc1b.get(key)] = 1.0;
            }
        }
        return new Coordinates(matrix, hsc1);
    }

//    /**
//     * 获取节点与坐标的距离关系，进一步去转化为二进制二维矩阵
//     *
//     * @param strList1 所有网关节点集合
//     * @param strList2 所有传感器节点集合
//     * @return
//     */
//    public Map<String, Map<String, Vector<Double>>> test02(List<String> strList1, List<String> strList2,String flag) throws Exception {
//        HashMap<String, Vector<Double>> hs1 = new HashMap<>();
//        HashMap<String, Vector<Double>> hs2 = new HashMap<>();
//
//        HashSet<String> hashSet = new HashSet<>();
//
//
//        for (int i = 0; i < strList1.size(); i++) {
//            Vector<Double> vc = new Vector<>();
//            //System.out.println(strList1.get(i));
//            String[] split = strList1.get(i).split(",");
//            vc.add(Double.parseDouble(split[1]));
//            vc.add(Double.parseDouble(split[2]));
//            hs1.put(split[0], vc);
//        }
//
//        for (int i = 0; i < strList2.size(); i++) {
//            Vector<Double> vc = new Vector<>();
//            //System.out.println(strList2.get(i));
//            String[] split = strList2.get(i).split(",");
//            vc.add(Double.parseDouble(split[1]));
//            vc.add(Double.parseDouble(split[2]));
//            hashSet.add(split[0]);
//            hs2.put(split[0], vc);
//        }
//
//        double raius = relatedProperties.getGatewayRadius();
//
//        // 计算路口集合
//        HashMap<String, Vector<Double>> sCrossingMap = countCrossing();
//
//        //TODO
//        //计算数据（与线性规划计算有关）, 这里分包含路口的计算和不包含路口的计算,路口集合在293行
//        // 一个函数做重载，差别在第三个参数有没有传入路口集合
//        // countSet(Map<String, Vector<Double>> gateway, Map<String, Vector<Double>> sensor,  double radius)
//        // countSet(Map<String, Vector<Double>> gateway, Map<String, Vector<Double>> sensor, Map<String, Vector<Double>> cros, double radius)
//        Map<String, Map<String, Vector<Double>>> stringMapMap = null;
//        if("withoutCros".equals(flag)){
//            stringMapMap = countSet(hs1, hs2, raius);
//        }else{
//            stringMapMap = countSet(hs1, hs2, sCrossingMap, raius);
//        }
//
//        HashMap<String, HashSet<String>> stringHashSetHashMap = new HashMap<>();
//
//        for (Map.Entry<String, Map<String, Vector<Double>>> entryt : stringMapMap.entrySet()) {
//            Map<String, Vector<Double>> valu2e = entryt.getValue();
//            HashSet<String> hashss = new HashSet<>();
//            for (Map.Entry<String, Vector<Double>> maps : valu2e.entrySet()) {
//                String key = maps.getKey();
//                hashss.add(key);
//            }
//            stringHashSetHashMap.put(entryt.getKey(), hashss);
//        }
//
//        //copy一下map，为了后面验证准确度
//        HashMap<String, HashSet<String>> lampposts2 = Detection.copyMap(stringHashSetHashMap);
//
//        //计算当前的所有灯柱能否包含所有传感器
//        HashSet<String> resultash = new HashSet<>();
//        for (Map.Entry<String, HashSet<String>> entry : lampposts2.entrySet()) {
//            HashSet<String> value = entry.getValue();
//            Iterator<String> iterator1 = value.iterator();
//            while (iterator1.hasNext()) {
//                resultash.add(iterator1.next());
//            }
//        }
//
//        if (resultash.size() != hashSet.size()) {
//            throw new Exception("传感器没有被全部覆盖到!");
//        }
//        return stringMapMap;
//    }

    /**
     * 调用matlab朴素贪心
     * @param strList1 网关
     * @param strList2 传感器
     */
    public ArrayList<String> test01(List<String> strList1, List<String> strList2, String flag) throws Exception {
        // 由网关集与传感器集获得matrix 与 hsc1
        Coordinates myCoor = getMatrixHsc1(strList1, strList2, flag);
        double[][] matrix = myCoor.getMatrix();
        HashMap<Integer, String> hsc1 = myCoor.getHsc1();
        // matlab朴素贪心
        MWNumericArray input = new MWNumericArray(matrix, MWClassID.DOUBLE);
        select_random_greedy_zsj.Class1 test = new select_random_greedy_zsj.Class1();
        Object[] sresult = test.select_random_greedy_zsj(2, input);

        int[] sol = ((MWNumericArray)sresult[0]).getIntData();

//        打印结果
//        for (int i : sol) {
//            System.out.print(i);
//            System.out.print(" ");
//        }
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < sol.length; i++){
            if(sol[i] == 1){
                result.add(hsc1.get(i));
            }
        }

        return result;
    }

    /**
     * 调用matlab分支限界代码
     * @param strList1 网关
     * @param strList2 传感器
     */
    public ArrayList<String> calByBB(List<String> strList1, List<String> strList2, String flag) throws Exception {
        // 由网关集与传感器集获得matrix 与 hsc1
        Coordinates myCoor = getMatrixHsc1(strList1, strList2, flag);
        double[][] matrix = myCoor.getMatrix();
        HashMap<Integer, String> hsc1 = myCoor.getHsc1();
        //分支限界 Matlab代码测试
        MWNumericArray input = new MWNumericArray(matrix, MWClassID.DOUBLE);
        branch_bound_algorithm.Class1 test = new branch_bound_algorithm.Class1();
        Object[] sresult = test.branch_bound_algorithm(2, input);
        int[] sol = ((MWNumericArray)sresult[0]).getIntData();

        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < sol.length; i++){
            if(sol[i] == 1){
                result.add(hsc1.get(i));
            }
        }
        return result;
    }



    /**
     * 有向贪心
     * @param strList1 网关
     * @param strList2 传感器
     */
    public ArrayList<String> test03(List<String> strList1, List<String> strList2,String flag) throws Exception {
        HashMap<String, Vector<Double>> hs1 = new HashMap<>();
        HashMap<String, Vector<Double>> hs2 = new HashMap<>();

        HashSet<String> hashSet = new HashSet<>();


        for (int i = 0; i < strList1.size(); i++) {
            Vector<Double> vc = new Vector<>();
//            System.out.println(strList1.get(i));
            String[] split = strList1.get(i).split(",");
            vc.add(Double.parseDouble(split[1]));
            vc.add(Double.parseDouble(split[2]));
            hs1.put(split[0], vc);
        }
        for (int i = 0; i < strList2.size(); i++) {
            Vector<Double> vc = new Vector<>();
            //System.out.println(strList2.get(i));
            String[] split = strList2.get(i).split(",");
            vc.add(Double.parseDouble(split[1]));
            vc.add(Double.parseDouble(split[2]));
            hashSet.add(split[0]);
            hs2.put(split[0], vc);
        }

        double raius = relatedProperties.getGatewayRadius();
        // 计算路口集合
        HashMap<String, Vector<Double>> crossingMap = countCrossing();

        //TODO
        //计算数据（与贪心计算有关），这里分包含路口的计算和不包含路口的计算
        Map<String, Map<String, Vector<Double>>> stringMapMap = null;
        if("withoutCros".equals(flag)){
            stringMapMap = countSet(hs1, hs2, raius);
        }else{
            stringMapMap = countSet(hs1, hs2, crossingMap, raius);
        }

        HashMap<String, HashSet<String>> stringHashSetHashMap = new HashMap<>();

        for (Map.Entry<String, Map<String, Vector<Double>>> entryt : stringMapMap.entrySet()) {
            Map<String, Vector<Double>> valu2e = entryt.getValue();
            HashSet<String> hashss = new HashSet<>();
            for (Map.Entry<String, Vector<Double>> maps : valu2e.entrySet()) {
                String key = maps.getKey();
                hashss.add(key);
            }
            stringHashSetHashMap.put(entryt.getKey(), hashss);
        }
        //计算当前的所有灯柱能否包含所有传感器
        HashSet<String> resultash = new HashSet<>();
        for (Map.Entry<String, HashSet<String>> entry : stringHashSetHashMap.entrySet()) {
            HashSet<String> value = entry.getValue();
            Iterator<String> iterator1 = value.iterator();
            while (iterator1.hasNext()) {
                resultash.add(iterator1.next());
            }
        }

        // 判断传感器有没有被全部覆盖到
        if (resultash.size() != hashSet.size()) {
            throw new Exception("传感器没有被全部覆盖到!");
        }
        // 调用Detection中的近似贪心算法计算
        ArrayList<String> result = Detection.calByLinner(strList2, strList1, stringHashSetHashMap);
//        System.out.println(result);
        return result;
    }

    /**
     * 蚁群
     * @param strList1 网关
     * @param strList2 传感器
     */
    public ArrayList<String> test04(List<String> strList1, List<String> strList2, String flag) throws Exception {
        // 由网关集与传感器集获得matrix 与 hsc1
        Coordinates myCoor = getMatrixHsc1(strList1, strList2, flag);
        double[][] doubleMatrix = myCoor.getMatrix();
        boolean[][] matrix = doubleToBool.trans(doubleMatrix);
        HashMap<Integer, String> hsc1 = myCoor.getHsc1();
        // 调用蚁群算法模块
        SCProblem problem = MatConvert.Mat_to_SCP(matrix);
        AlgorithmConfiguration_self Alg_config = new AlgorithmConfiguration_self();
        AbstractAlgorithm aco_algorithm = Alg_config.create(problem);

        // ACO算法开始执行
        aco_algorithm.run();
        // 获取统计信息
        Statistics statistics = aco_algorithm.getStatistics();
        SCPSolution solution = (SCPSolution) statistics.getGlobalMinSolution();
        boolean[] sol = solution.getVars();
        //打印结果
//        for (boolean i : sol) {
//            System.out.print(i);
//            System.out.print(" ");
//        }
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < sol.length; i++){
            if(sol[i]){
                result.add(hsc1.get(i));
            }
        }

        return result;
    }

    /**
     * 调用 matlab 线性规划
     * @param strList1 网关
     * @param strList2 传感器
     */
    public ArrayList<String> calByMatLP(List<String> strList1, List<String> strList2, String flag) throws Exception {
        // 由网关集与传感器集获得matrix 与 hsc1
        Coordinates myCoor = getMatrixHsc1(strList1, strList2, flag);
        double[][] matrix = myCoor.getMatrix();
        HashMap<Integer, String> hsc1 = myCoor.getHsc1();
        // 调用matlab计算
        MWNumericArray input = new MWNumericArray(matrix, MWClassID.DOUBLE);
        select_linprog.Class1 test = new select_linprog.Class1();
        Object[] sresult = test.select_linprog(2, input);
        int[] sol = ((MWNumericArray)sresult[0]).getIntData();

//        打印结果
//        for (int i : sol) {
//            System.out.print(i);
//            System.out.print(" ");
//        }
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < sol.length; i++){
            if(sol[i] == 1){
                result.add(hsc1.get(i));
            }
        }

        return result;
    }
    /**
     * ortools 线性规划
     * @param strList1 网关
     * @param strList2 传感器
     */

    public ArrayList<String> calByorLP(List<String> strList1, List<String> strList2, String flag) throws Exception {
        // 由网关集与传感器集获得matrix 与 hsc1
        Coordinates myCoor = getMatrixHsc1(strList1, strList2, flag);
        double[][] matrix = myCoor.getMatrix();
        HashMap<Integer, String> hsc1 = myCoor.getHsc1();

        int[] sol = cal_LP.linprog(matrix);

        // 调用matlab计算
//        MWNumericArray input = new MWNumericArray(matrix, MWClassID.DOUBLE);
//        select_linprog.Class1 test = new select_linprog.Class1();
//        Object[] sresult = test.select_linprog(2, input);
//        int[] sol = ((MWNumericArray)sresult[0]).getIntData();

//        打印结果
//        for (int i : sol) {
//            System.out.print(i);
//            System.out.print(" ");
//        }
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < sol.length; i++){
            if(sol[i] == 1){
                result.add(hsc1.get(i));
            }
        }

        return result;
    }

    /**
     * 遗传算法
     */
    public ArrayList<String> test06(List<String> strList1, List<String> strList2, String flag) throws Exception {
        // 由网关集与传感器集获得matrix 与 hsc1
        Coordinates myCoor = getMatrixHsc1(strList1, strList2, flag);
        double[][] matrix = myCoor.getMatrix();
        HashMap<Integer, String> hsc1 = myCoor.getHsc1();
//        遗传算法
        MWNumericArray input = new MWNumericArray(matrix, MWClassID.DOUBLE);
        GA_parse.Class1 test = new GA_parse.Class1();
        Object[] sresult = test.GA_parse(2, input);
        int[] sol = ((MWNumericArray)sresult[0]).getIntData();

//        打印结果
//        for (int i : sol) {
//            System.out.print(i);
//            System.out.print(" ");
//        }
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < sol.length; i++){
            if(sol[i] == 1){
                result.add(hsc1.get(i));
            }
        }

        return result;
    }

//    /**
//     * 计算每个摄像头能包含的节点集合
//     *
//     * @param gateway
//     * @param sensor
//     * @param angle
//     * @return
//     */
//    public Map<String, Map<String, Vector<Double>>> countSetCm(Map<String, Vector<Double>> gateway, Map<String, Vector<Double>> sensor, double angle) {
//
//        Map<String, Map<String, Vector<Double>>> coverMap = new HashMap<>();
//
//        for (Map.Entry<String, Vector<Double>> entry_gateway : gateway.entrySet()) {
//
//            // 计算每个灯柱的x，y坐标,高度
//            Vector<Double> gateway_vc = entry_gateway.getValue();
//            double x_gateway_vc = gateway_vc.get(0);
//            BigDecimal bigDecimal_gateway_x = new BigDecimal(Double.valueOf(x_gateway_vc));
//            double y_gateway_vc = gateway_vc.get(1);
//            BigDecimal bigDecimal_gateway_y = new BigDecimal(Double.valueOf(y_gateway_vc));
//            //高度
//            String tempS = entry_gateway.getKey();
//            String[] tempSA = tempS.split("_");
//            double h_gateway_vc = Double.parseDouble(tempSA[1]);
//
//            // 新建一个map存放每个网关所能覆盖的节点
//            Map<String, Vector<Double>> coverMap_gateway = new HashMap<>();
//
//            for (Map.Entry<String, Vector<Double>> entry_sensor : sensor.entrySet()) {
//
//                // 获取每个节点的x，y坐标
//                Vector<Double> sensor_vc = entry_sensor.getValue();
//                double x_sensor_vc = sensor_vc.get(0);
//                BigDecimal bigDecimal_sensor_x = new BigDecimal(Double.valueOf(x_sensor_vc));
//                double y_sensor_vc = sensor_vc.get(1);
//                BigDecimal bigDecimal_sensor_y = new BigDecimal(Double.valueOf(y_sensor_vc));
//
//                //计算摄像头的覆盖盲区
//                double min_distance = h_gateway_vc * Math.tan(Math.PI * 15 / 180);
//
//                //计算摄像头的覆盖区域
//                double max_distance = h_gateway_vc * Math.tan(Math.PI * (angle + 15) / 180);
//
//                // 计算摄像头与节点之间的距离
//                BigDecimal subtract_x = bigDecimal_sensor_x.subtract(bigDecimal_gateway_x);
//                BigDecimal subtract_y = bigDecimal_sensor_y.subtract(bigDecimal_gateway_y);
//                BigDecimal multiply_x = subtract_x.multiply(subtract_x);
//                BigDecimal multiply_y = subtract_y.multiply(subtract_y);
//                BigDecimal add = multiply_x.add(multiply_y);
//
//
//                //使用牛顿迭代法计算平方根
//                BigDecimal sqrt = sqrt(add);
//                double distance = sqrt.doubleValue();
//                double aa_distance = distance * Math.pow(10, 5);
//                String string_gateway = entry_gateway.getKey().substring(0, 5);
//                String string_sensor = entry_sensor.getKey().substring(0, 5);
//
//                // 如果距离在当前网关的覆盖之内，则把当前节点加入到当前网关的map集合 * 号代表路口的传感器节点  同一条路&& string_gateway.equals(string_sensor)
//                if (x_gateway_vc < x_sensor_vc && aa_distance < max_distance && aa_distance > min_distance && string_gateway.equals(string_sensor)) {
//                    coverMap_gateway.put(entry_sensor.getKey(), entry_sensor.getValue());
//                }
//            }
//
//            // 把当前网关和它所能覆盖到的节点加入到map集合
//            coverMap.put(entry_gateway.getKey(), coverMap_gateway);
//        }
//        return coverMap;
//    }

//    /**
//     * 生成python数据 摄像头
//     *
//     * @param gateWayList 顾名思义
//     * @param sensorList  顾名思义
//     */
//    public void createPyDataCm(List<String> gateWayList, List<String> sensorList) throws Exception {
//        String property = System.getProperty("user.dir");
//        BufferedWriter bfs = null;
//        BufferedWriter bfg = null;
//
//        //生成py网关数据
//        try {
//            bfs = new BufferedWriter(new FileWriter(property + "\\src\\main\\java\\com\\example\\system\\demo\\calculate\\gateWayData.txt"));
//            //bfg = new BufferedWriter(new FileWriter("C:\\Users\\THTF\\Desktop\\gateway\\gateWayData.txt"));
//            Map<String, Map<String, Vector<Double>>> result = test02Cm(gateWayList, sensorList);
//            // 灯柱包含的传感器的集合 写入文件
//            for (Map.Entry<String, Map<String, Vector<Double>>> keyValue : result.entrySet()) {
//                Map<String, Vector<Double>> value = keyValue.getValue();
//                bfs.write(keyValue.getKey() + " ");
//                for (String key : value.keySet()) {
//                    bfs.write(key + " ");
//                }
//                bfs.newLine();
//            }
//            bfs.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bfs != null) bfs.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //生成py传感器数据
//        try {
//            bfg = new BufferedWriter(new FileWriter(property + "\\src\\main\\java\\com\\example\\system\\demo\\calculate\\sensorData.txt"));
//            for (String s : sensorList) {
//                String[] split = s.split(",");
//                bfg.write(split[0]);
//                bfg.newLine();
//            }
//            bfg.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bfg != null) bfs.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public Map<String, Map<String, Vector<Double>>> test02Cm(List<String> strList1, List<String> strList2) throws Exception {
//        HashMap<String, Vector<Double>> hs1 = new HashMap<>();
//        HashMap<String, Vector<Double>> hs2 = new HashMap<>();
//
//        HashSet<String> hashSet = new HashSet<>();
//
////        List<String> strList1 = getFileContent("C:\\Users\\THTF\\Desktop\\gateway\\all.txt");
////        List<String> strList2= getFileContent("C:\\Users\\THTF\\Desktop\\sensor\\All_road.txt");
//
//        for (int i = 0; i < strList1.size(); i++) {
//            Vector<Double> vc = new Vector<>();
//            //System.out.println(strList1.get(i));
//            String[] split = strList1.get(i).split(",");
//            vc.add(Double.parseDouble(split[1]));
//            vc.add(Double.parseDouble(split[2]));
//            //vc.add(Double.parseDouble(split[3]));
//            hs1.put(split[0], vc);
//        }
//
//        for (int i = 0; i < strList2.size(); i++) {
//            Vector<Double> vc = new Vector<>();
//            //System.out.println(strList2.get(i));
//            String[] split = strList2.get(i).split(",");
//            vc.add(Double.parseDouble(split[1]));
//            vc.add(Double.parseDouble(split[2]));
//            hashSet.add(split[0]);
//            hs2.put(split[0], vc);
//        }
//        double angle = relatedProperties.getGatewayAngle();
//        //计算数据,转换为 贪心算法的格式
//        Map<String, Map<String, Vector<Double>>> stringMapMap = countSetCm(hs1, hs2, angle);
//
//        HashMap<String, HashSet<String>> stringHashSetHashMap = new HashMap<>();
//
//        for (Map.Entry<String, Map<String, Vector<Double>>> entryt : stringMapMap.entrySet()) {
//            Map<String, Vector<Double>> valu2e = entryt.getValue();
//            HashSet<String> hashss = new HashSet<>();
//            for (Map.Entry<String, Vector<Double>> maps : valu2e.entrySet()) {
//                String key = maps.getKey();
//                hashss.add(key);
//            }
//            stringHashSetHashMap.put(entryt.getKey(), hashss);
//        }
//
//        //copy一下map，为了后面验证准确度
//        HashMap<String, HashSet<String>> lampposts2 = Detection.copyMap(stringHashSetHashMap);
//
//        //计算当前的所有灯柱能否包含所有传感器
//        HashSet<String> resultash = new HashSet<>();
//        for (Map.Entry<String, HashSet<String>> entry : lampposts2.entrySet()) {
//            HashSet<String> value = entry.getValue();
//            Iterator<String> iterator1 = value.iterator();
//            while (iterator1.hasNext()) {
//                resultash.add(iterator1.next());
//            }
//        }
//
//        if (resultash.size() != hashSet.size()) {
//            throw new Exception("传感器没有被全部覆盖到!");
//        }
//
//        return stringMapMap;
//    }

//    /**
//     * @param strList1 摄像头
//     * @param strList2 传感器
//     */
//    public ArrayList<String> test01Cm(List<String> strList1, List<String> strList2) throws Exception {
//        HashMap<String, Vector<Double>> hs1 = new HashMap<>();
//        HashMap<String, Vector<Double>> hs2 = new HashMap<>();
//
//        HashSet<String> hashSet = new HashSet<>();
//
////        List<String> strList1 = getFileContent("D:\\downloads\\灯柱坐标数据\\已标记\\all.txt");
////        List<String> strList2= getFileContent("D:\\gatewayData\\All_road.txt");
//
//        for (int i = 0; i < strList1.size(); i++) {
//            Vector<Double> vc = new Vector<>();
//            //System.out.println(strList1.get(i));
//            String[] split = strList1.get(i).split(",");
//            vc.add(Double.parseDouble(split[1]));
//            vc.add(Double.parseDouble(split[2]));
//            //vc.add(Double.parseDouble(split[3]));
//            hs1.put(split[0], vc);
//        }
//
//        for (int i = 0; i < strList2.size(); i++) {
//            Vector<Double> vc = new Vector<>();
//            //System.out.println(strList2.get(i));
//            String[] split = strList2.get(i).split(",");
//            vc.add(Double.parseDouble(split[1]));
//            vc.add(Double.parseDouble(split[2]));
//            hashSet.add(split[0]);
//            hs2.put(split[0], vc);
//        }
//
//        double angle = relatedProperties.getGatewayAngle();
//
//        //计算数据,转换为 贪心算法的格式
//        Map<String, Map<String, Vector<Double>>> stringMapMap = countSetCm(hs1, hs2, angle);
//
//        HashMap<String, HashSet<String>> stringHashSetHashMap = new HashMap<>();
//
//        for (Map.Entry<String, Map<String, Vector<Double>>> entryt : stringMapMap.entrySet()) {
//            Map<String, Vector<Double>> valu2e = entryt.getValue();
//            HashSet<String> hashss = new HashSet<>();
//            for (Map.Entry<String, Vector<Double>> maps : valu2e.entrySet()) {
//                String key = maps.getKey();
//                hashss.add(key);
//            }
//            stringHashSetHashMap.put(entryt.getKey(), hashss);
//        }
//
//        //copy一下map，为了后面验证准确度
//        HashMap<String, HashSet<String>> lampposts2 = Detection.copyMap(stringHashSetHashMap);
//
//        //计算当前的所有灯柱能否包含所有传感器
//        HashSet<String> resultash = new HashSet<>();
//        for (Map.Entry<String, HashSet<String>> entry : lampposts2.entrySet()) {
//            HashSet<String> value = entry.getValue();
//            Iterator<String> iterator1 = value.iterator();
//            while (iterator1.hasNext()) {
//                resultash.add(iterator1.next());
//            }
//        }
//
//        if (resultash.size() != hashSet.size()) {
//            throw new Exception("传感器没有被全部覆盖到!");
//        }
//
//        // 调用Detection中的近似贪心算法计算
//        ArrayList<String> result = Detection.greedyAlgorithm(hashSet, stringHashSetHashMap);
//        return result;
//    }

}
