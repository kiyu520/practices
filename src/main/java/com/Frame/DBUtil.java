package com.Frame;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {
    // 数据库连接参数（根据自己的数据库修改）
    private static final String URL = "jdbc:mysql://localhost:3306/warehouse_db?useUnicode=true&characterEncoding=utf8&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    // 获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭连接
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}