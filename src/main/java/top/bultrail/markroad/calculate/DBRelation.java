package top.bultrail.markroad.calculate;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import top.bultrail.markroad.bean.Point;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

@Component
public class DBRelation {

    private QueryRunner qr = new QueryRunner();

    //新建连接
    private Connection setConnection() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        Connection conn;
        try {
            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
            // 用户 mytest
//            cpds.setJdbcUrl("jdbc:mysql://10.45.39.13:3306/Marks");
//            cpds.setUser("mytest");
//            cpds.setPassword("12345678");

            // 用户 mytest2
            cpds.setJdbcUrl("jdbc:mysql://10.45.39.13:3306/Marks2");
            cpds.setUser("mytest2");
            cpds.setPassword("12345Qq@");

            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(10);
            conn = cpds.getConnection();
        } catch (PropertyVetoException | SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    //写入数据 新增
    public void write_new(String roadName, List<String[]> strList1, String pointType) {
        Connection conn = setConnection();
        if ("sensor".equals(pointType)) {
            for (int i = 0; i < strList1.size(); i++) {
                try {
                    //准备SQL语句
                    String sql = "INSERT INTO sensor(roadName,numberInRoad,Lng,Lat) VALUES (?, ?, ?, ?)";
//                    System.out.println(sql);
                    Object[] obj = {roadName, i, strList1.get(i)[0], strList1.get(i)[1]};
                    //执行，抛出异常
                    qr.update(conn, sql, obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if ("gateway".equals(pointType)) {
            for (int i = 0; i < strList1.size(); i++) {
                try {
                    String sql = "INSERT INTO gateway(roadName,numberInRoad,Lng,Lat) VALUES (?, ?, ?, ?)";
//                    System.out.println(sql);
                    Object[] obj = {roadName, i, strList1.get(i)[0], strList1.get(i)[1]};
                    //执行，抛出异常
                    qr.update(conn, sql, obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if ("cros".equals(pointType)) {
            for (int i = 0; i < strList1.size(); i++) {
                try {
                    String sql = "INSERT INTO crossing(roadName,numberInRoad,Lng,Lat) VALUES (?, ?, ?, ?)";
//                    System.out.println(sql);
                    Object[] obj = {roadName, i, strList1.get(i)[0], strList1.get(i)[1]};
                    //执行，抛出异常
                    qr.update(conn, sql, obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //清空数据表 新增
    public void clear() {
        Connection conn = setConnection();
        try {
            qr.update(conn, "TRUNCATE TABLE sensor");
            qr.update(conn, "TRUNCATE TABLE gateway");
            qr.update(conn, "TRUNCATE TABLE crossing");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //排序 新增
    public void sort() {
        Connection conn = null;
        String[] keys = new String[]{"sensor", "gateway", "crossing"};
        for (String key : keys) {
            String sql = "SELECT * FROM " + key + " ORDER BY numberInRoad, " + key + "_id";
            try {
                conn = setConnection();
                List<Point> rs = qr.query(conn, sql, new BeanListHandler<>(Point.class));
                qr.update(conn, "TRUNCATE TABLE " + key);
//                System.out.println(rs.size());
                for(Point p : rs) {
//                    System.out.println(p);
                    String sql2 = "INSERT INTO " + key +"(roadName,numberInRoad,Lng,Lat) VALUES (?, ?, ?, ?)";
                    Object[] obj = {p.getRoadName(), p.getNumberInRoad(), p.getLng(), p.getLat()};
                    qr.update(conn, sql2, obj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //备份 新增
    public void bkup() {

        Connection conn = setConnection();
        String[] keys = new String[]{"sensor", "gateway", "crossing"};
        for (String key : keys) {
            String sql = "INSERT INTO " + key + "_backup(roadName, numberInRoad, Lng, Lat, road_id) SELECT roadName, numberInRoad, Lng, Lat, road_id FROM " + key;
            try {
                qr.update(conn, sql);
                }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tdata(String num) {

        Connection conn = setConnection();
        String[] keys = new String[]{"sensor", "gateway", "crossing"};
        for (String key : keys) {
            String sql = "INSERT INTO " + key + " SELECT * FROM " + key + "_" + num;
            try {
                qr.update(conn, sql);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tdataG(String num) {

        Connection conn = setConnection();
        String[] keys = new String[]{"sensor", "gateway", "crossing"};
        for (String key : keys) {
            String sql = "INSERT INTO " + key + " SELECT * FROM " + key + "_guo" + num;
            try {
                qr.update(conn, sql);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void res() {

        Connection conn = setConnection();
        String[] keys = new String[]{"sensor", "gateway", "crossing"};
        for (String key : keys) {

            String sql = "INSERT INTO " + key + " SELECT * FROM " + key + "_backup";
            try {
                qr.update(conn, sql);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    读取senor 新增
    public List<List<String>> readSensor() {

        String sql = "SELECT * FROM sensor";
        List<String> lis;
        List<List<String>> bk = new ArrayList();
        try {
            Connection conn = setConnection();
            List<Point> rs = qr.query(conn, sql, new BeanListHandler<>(Point.class));
            for(Point p : rs) {
                lis = new ArrayList();
                lis.add(p.getRoadName());
                lis.add(p.getNumberInRoad());
                lis.add(p.getLng());
                lis.add(p.getLat());
                bk.add(lis);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bk;
    }

    public List<List<String>> readGateway() {

        String sql = "SELECT * FROM gateway";
        List<String> lis;
        List<List<String>> bk = new ArrayList();
        try {
            Connection conn = setConnection();
            List<Point> rs = qr.query(conn, sql, new BeanListHandler<>(Point.class));
            for(Point p : rs) {
                lis = new ArrayList();
                lis.add(p.getRoadName());
                lis.add(p.getNumberInRoad());
                lis.add(p.getLng());
                lis.add(p.getLat());
                bk.add(lis);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bk;
    }

    public List<List<String>> readCrossing() {

        String sql = "SELECT * FROM crossing";
        List<String> lis;
        List<List<String>> bk = new ArrayList();
        try {
            Connection conn = setConnection();
            List<Point> rs = qr.query(conn, sql, new BeanListHandler<>(Point.class));
            for(Point p : rs) {
                lis = new ArrayList();
                lis.add(p.getRoadName());
                lis.add(p.getNumberInRoad());
                lis.add(p.getLng());
                lis.add(p.getLat());
                bk.add(lis);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bk;
    }
}
