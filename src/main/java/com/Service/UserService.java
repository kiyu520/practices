package com.Service;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Tools.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class UserService {


    public User login(String username, String password) {
        // 1. 通过工具类获取 SqlSession
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

        // 2. 调用Mapper查询用户
        User user = usermapper.select_user_by_name(username);

        // 3. 密码匹配校验
        if (user != null && user.getPassword().equals(password)) {
            MyBatisUtil.closeSqlSession(sqlSession);
            return user;
        }

        MyBatisUtil.closeSqlSession(sqlSession);
        return null;
    }

    public boolean changePassword(String username, String oldPwd, String newPwd) {
        // 1. 入参非空校验
        if (username == null || oldPwd == null || newPwd == null) {
            return false;
        }

        // 2. 获取 SqlSession
        SqlSession sqlSession = MyBatisUtil.getSqlSession();
        user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

        // 3. 查询用户并校验原密码
        User user = usermapper.select_user_by_name(username);
        if (user == null || !user.getPassword().equals(oldPwd)) {
            MyBatisUtil.closeSqlSession(sqlSession);
            return false;
        }

        // 4. 构建修改对象并更新密码
        User updateUser = new User();
        updateUser.setUsername(username);
        updateUser.setPassword(newPwd);
        updateUser.setUserRole(user.getUserRole());

        int rows = usermapper.update_user_by_name(updateUser);
        MyBatisUtil.closeSqlSession(sqlSession);

        // 5. 返回结果
        return rows > 0;
    }
}