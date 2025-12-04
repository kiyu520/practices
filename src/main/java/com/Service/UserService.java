package com.Service;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;

import static com.Tools.SqlUtil.session;

public class UserService {
    public User login(String username, String password) {
        SqlSession sqlSession = null;
        try {
            // 1. 通过工具类获取 SqlSession
            sqlSession = SqlUtil.getSession();
            user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

            // 2. 调用Mapper查询用户
            User user = usermapper.select_user_by_name(username);

            // 3. 密码匹配校验
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 无论try成功/失败、catch是否触发，都会执行这里
            if (sqlSession != null) {
                sqlSession.close(); // 释放当前连接，避免泄漏
            }
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