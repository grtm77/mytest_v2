package com.example.system.demo.calculate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.system.demo.config.RelatedProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBRelation {

    @Autowired
    RelatedProperties relatedProperties;

    @Autowired
    CountSet countSet;

    public void write_new(String roadName, List<String[]> strList1, String pointType) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 1.加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2.创建连接
            conn = DriverManager.getConnection
                    ("jdbc:mysql://127.0.0.1:3306/Marks?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT&user=root&password=T197lyjZ148");
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if ("sensor".equals(pointType)) {
            for (int i = 0; i < strList1.size(); i++) {
                try {
                    String tsql = "INSERT INTO sensor(roadName,numberInRoad,Lng,Lat) VALUES (" + roadName + "," + i + "," + strList1.get(i)[0] + "," + strList1.get(i)[1] + ")";
                    stmt.executeUpdate(tsql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if ("gateway".equals(pointType)) {
            for (int i = 0; i < strList1.size(); i++) {
                try {
                    String tsql = "INSERT INTO gateway(roadName,numberInRoad,Lng,Lat) VALUES (" + roadName + "," + i + "," + strList1.get(i)[0] + "," + strList1.get(i)[1] + ")";
                    stmt.executeUpdate(tsql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else if ("cros".equals(pointType)) {
            for (int i = 0; i < strList1.size(); i++) {
                try {
                    String tsql = "INSERT INTO crossing(roadName,numberInRoad,Lng,Lat) VALUES (" + roadName + "," + i + "," + strList1.get(i)[0] + "," + strList1.get(i)[1] + ")";
                    stmt.executeUpdate(tsql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clear() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 创建连接
            conn = DriverManager.getConnection
                    ("jdbc:mysql://127.0.0.1:3306/Marks?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT&user=root&password=T197lyjZ148");
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stmt.executeUpdate("TRUNCATE TABLE sensor");
            stmt.executeUpdate("TRUNCATE TABLE gateway");
            stmt.executeUpdate("TRUNCATE TABLE crossing");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> readSensor() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 加载数据库驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 创建连接
            conn = DriverManager.getConnection
                    ("jdbc:mysql://127.0.0.1:3306/Marks?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT&user=root&password=T197lyjZ148");
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String tsql = "SELECT * FROM sensor";
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
}
