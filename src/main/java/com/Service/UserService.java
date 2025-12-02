//package com.Service;
//
//import com.Entity.User;
//
//public class UserService {
//    private UserDAO userdao = new UserDAO();
//    public User login(String username, String password){
////        1.调用mapper查询用户
//        User user = userDAO.findByUsername(username);
////        2.密码匹配
//        if(user != null && user.getPassword().equals(password)){
//            return user;
//        }
//        return null;
//    }
//
//}
////原密码正确才能修改密码
//public boolean changePassword(String username.String oldPwd String newPwd){
////    1.校验原密码
//    User user = userDAO.findByUsername(username);
//    if(user != null || !user.getPassword().equals(oldPwd)){
//        return false;
//    }
////    修改密码
//    return userDAO.updatePassword(username,newPwd);
//}
