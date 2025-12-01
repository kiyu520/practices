package com.Tools;

import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

@Log
public class SqlUtil {
    private static  SqlSessionFactory  sqlSessionFactory;
    static{
        try {
            sqlSessionFactory= new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            log.severe("数据库工具连接失败");
        }
    }
    public static SqlSession getSession(boolean autoCommit) {
        log.info("获取数据库连接，自动提交:"+autoCommit);
        return  sqlSessionFactory.openSession(autoCommit);
    }
}
