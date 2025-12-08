package com.Service;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

import static com.Tools.SqlUtil.sqlSessionFactory;

public class UserService {
    /**
     * 用户登录方法
     * @param username 用户名
     * @param password 密码
     * @return User对象，登录成功返回用户信息，失败返回null
     */
    public User login(String username, String password) {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlUtil.getSession();  // 从工具类获取数据库连接会话
            // 创建Mapper接口的代理对象
            user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

            // 根据用户名查询用户信息
            User user = usermapper.select_user_by_name(username);
            // 检查用户是否存在且密码匹配
            if (user != null && user.getPassword().equals(password)) {
                return user;  // 登录成功，返回用户信息
            }
            return null;  // 登录失败，返回null
        } catch (Exception e) {
            // 捕获并打印异常信息
            System.err.println("用户名：" + username + "，异常类型：" + e.getClass().getSimpleName() + "，原因：" + e.getMessage());
            e.printStackTrace();
            return null;  // 发生异常时返回null
        }
    }

    public boolean changePassword(String username, String oldPwd) {
        // 1. 入参非空校验
        String newPwd= String.valueOf(' ');
        if (username == null || oldPwd == null || newPwd == null) {
            return false;
        }

        // 2. 获取 SqlSession
        SqlSession sqlSession = SqlUtil.getSession();
        user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

        // 3. 查询用户并校验原密码
        User user = usermapper.select_user_by_name(username);
        if (user == null || !user.getPassword().equals(oldPwd)) {
            return false;
        }

        // 4. 构建修改对象并更新密码
        User updateUser = new User();
        updateUser.setUsername(username);
        updateUser.setPassword(newPwd);
        updateUser.setUserRole(user.getUserRole());

        int rows = usermapper.update_user_by_name(updateUser);

        // 5. 返回结果
        return rows > 0;
    }
}