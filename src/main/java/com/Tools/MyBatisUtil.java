package com.Tools; // 包名与你项目的工具类路径一致

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * MyBatis 工具类：封装 SqlSessionFactory 和 SqlSession 的创建/关闭
 * 单例模式初始化 SqlSessionFactory，避免重复加载配置文件
 */
public class MyBatisUtil {
    // 静态单例 SqlSessionFactory（全局唯一）
    private static SqlSessionFactory sqlSessionFactory;

    // 静态代码块：初始化 SqlSessionFactory（项目启动时执行一次）
    static {
        try {
            // 加载 MyBatis 核心配置文件（路径：src/main/resources/mybatis-config.xml）
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            // 构建 SqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            // 初始化失败抛出运行时异常，由上层统一处理
            throw new RuntimeException("MyBatis 配置文件加载失败！", e);
        }
    }

    /**
     * 获取 SqlSession 对象（自动提交事务）
     * @return SqlSession 实例
     */
    public static SqlSession getSqlSession() {
        // openSession(true)：自动提交事务；false：手动提交（需调用 commit()）
        return sqlSessionFactory.openSession(true);
    }

    /**
     * 关闭 SqlSession 资源（避免连接泄漏）
     * @param sqlSession 需关闭的 SqlSession 实例
     */
    public static void closeSqlSession(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

    /**
     * 手动控制事务的 SqlSession 获取（适用于多操作事务场景）
     * @param autoCommit 是否自动提交（false=手动提交）
     * @return SqlSession 实例
     */
    public static SqlSession getSqlSession(boolean autoCommit) {
        return sqlSessionFactory.openSession(autoCommit);
    }

    // 私有化构造方法，禁止实例化工具类
    private MyBatisUtil() {}
}