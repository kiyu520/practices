package com.Entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private int userRole;
/**
 * 用户类的构造方法
 * @param username 用户名
 * @param password 密码
 * @param userRole 用户角色
 */
     public User(String username, String password, int userRole) {
    // 使用传入的参数初始化用户名
         this.username = username;
    // 使用传入的参数初始化密码
         this.password = password;
    // 使用传入的参数初始化用户角色
         this.userRole = userRole;
     }
     public String getUsername() {
         return username;
     }
     public void setUsername(String username) {
         this.username = username;
     }
     public String getPassword() {
         return password;
     }
     public void setPassword(String password) {
         this.password = password;
     }
     public int getUserRole() {
         return userRole;
     }
     public void setUserRole(int userRole) {
         this.userRole = userRole;
     }
}
