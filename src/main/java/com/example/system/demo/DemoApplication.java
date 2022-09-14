package com.example.system.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);

    }

}



//public class DemoApplication {
//    public static void main(String[] args) {
//        try {
//            // 1.加载数据库驱动类
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("数据库驱动加载成功");
//
//            // 2.创建连接
//            //格式为jdbc:mysql:
//            // 127.0.0.1:3306/数据库名称?useSSL=true&characterEncoding=utf-8&user=账号名&password=密码
//            Connection connection = DriverManager.getConnection
//                    ("jdbc:mysql://127.0.0.1:3306/Marks?useSSL=true&characterEncoding=utf-8&serverTimezone=GMT&user=root&password=T197lyjZ148");
//            System.out.println("创建连接成功");
//
//
//        }
//        catch (ClassNotFoundException cnfe) {
//            cnfe.printStackTrace();
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//        }
//    }
//}
