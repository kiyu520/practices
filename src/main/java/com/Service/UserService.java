package com.Service;

import com.Entity.User;
import com.Mappers.user_mapper;
import com.Tools.SqlUtil;
import org.apache.ibatis.session.SqlSession;

public class UserService {
    public User login(String username, String password) {
        // 1. 通过工具类获取 SqlSession
        SqlSession sqlSession = SqlUtil.getSession();
        user_mapper usermapper = sqlSession.getMapper(user_mapper.class);

        // 2. 调用Mapper查询用户
        User user = usermapper.select_user_by_name(username);

        // 3. 密码匹配校验
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
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