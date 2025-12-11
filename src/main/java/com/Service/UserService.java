package com.Service;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

public class UserService {
    public User login(String username, String password) {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlUtil.getSession();

            user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

            User user = usermapper.select_user_by_name(username);

            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        } catch (Exception e) {
            // 捕获并打印异常信息
            System.err.println("用户名：" + username + "，异常类型：" + e.getClass().getSimpleName() + "，原因：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean changePassword(String username, String oldPwd, String newPwd) {
        // 1. 入参非空校验
        if (username == null || oldPwd == null || newPwd == null ||
                username.isEmpty() || oldPwd.isEmpty() || newPwd.isEmpty()) {
            return false;
        }

        SqlSession sqlSession = null;
        try {
            sqlSession = SqlUtil.getSession();
            user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

            // 2. 查询用户并校验原密码
            User user = usermapper.select_user_by_name(username);
            if (user == null || !user.getPassword().equals(oldPwd)) {
                return false;
            }

            // 3. 构建修改对象并更新密码
            User updateUser = new User();
            updateUser.setUsername(username);
            updateUser.setPassword(newPwd);
            updateUser.setUserRole(user.getUserRole());

            int rows = usermapper.update_user_by_name(updateUser);
            sqlSession.commit(); // 手动提交事务

            // 4. 返回结果
            return rows > 0;
        } catch (Exception e) {
            if (sqlSession != null) {
                sqlSession.rollback(); // 异常回滚
            }
            System.err.println("修改密码异常-用户名：" + username + "，异常类型：" + e.getClass().getSimpleName() + "，原因：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (sqlSession != null) {
                sqlSession.close(); // 关闭SqlSession
            }
        }
    }
}