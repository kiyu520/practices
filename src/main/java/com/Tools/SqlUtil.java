package com.Tools;

import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

@Log
public class SqlUtil {
    public static  SqlSessionFactory  sqlSessionFactory;

    static{
        try {
            sqlSessionFactory= new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("Properties/mybatis-config.xml"));
            log.info("数据库初始化完成");
        } catch (IOException e) {
            log.severe("数据库工具连接失败");
        }
    }
    public static SqlSession getSession() {
        log.info("获取数据库连接");
        return sqlSessionFactory.openSession(true);
    }
}
