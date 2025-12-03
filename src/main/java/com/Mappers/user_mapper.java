package com.Mappers;

import com.Entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface user_mapper {
    // 字段映射模板：数据库下划线字段 → 实体类属性
    @Results( value = {
            @Result(column = "u_name", property = "username"),
            @Result(column = "u_password", property = "password"),
            @Result(column = "u_role", property = "userRole")
    })
    @Insert("insert into practice.users(u_name, u_password, u_role) " +
            "values(#{u.username}, #{u.password}, #{u.userRole})")
    int add_user_entity(@Param("u") User u);

    // 新增：传入单个参数
    @Insert("insert into practice.users(u_name, u_password, u_role) " +
            "values(#{username}, #{password}, #{userRole})")
    int add_user_args(@Param("username") String username,
                      @Param("password") String password,
                      @Param("userRole") int userRole);

    // 查询所有用户（复用userMap映射）
    @Results( value = {
            @Result(column = "u_name", property = "username"),
            @Result(column = "u_password", property = "password"),
            @Result(column = "u_role", property = "userRole")
    })
    @Select("select * from practice.users")
    List<User> select_user_all();

    // 按用户名查询
    @Results( value = {
            @Result(column = "u_name", property = "username"),
            @Result(column = "u_password", property = "password"),
            @Result(column = "u_role", property = "userRole")
    })
    @Select("select * from practice.users where u_name=#{username}")
    User select_user_by_name(String username);

    // 按用户角色查询
    @Results( value = {
            @Result(column = "u_name", property = "username"),
            @Result(column = "u_password", property = "password"),
            @Result(column = "u_role", property = "userRole")
    })
    @Select("select * from practice.users where u_role=#{userRole}")
    List<User> select_user_by_role(int userRole);

    // 修改：按用户名更新信息
    @Update("update practice.users set u_password=#{u.password}, u_role=#{u.userRole} " +
            "where u_name=#{u.username}")
    int update_user_by_name(@Param("u") User u);

    // 删除：按用户名删除
    @Delete("delete from practice.users where u_name=#{username}")
    int delete_user_by_name(String username);

    // 删除：按用户角色删除
    @Delete("delete from practice.users where u_role=#{userRole}")
    int delete_user_by_role(int userRole);
}