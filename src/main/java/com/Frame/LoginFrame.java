package com.Frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.Entity.User;
import com.Service.UserService;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserService userService = new UserService();

    public LoginFrame() {
        // 创建登录界面
        setTitle("郑州轻工业大学仓库管理系统 - 登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("仓库管理系统");
        titleLabel.setBounds(120, 30, 200, 30);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setBounds(80, 80, 60, 25);
        usernameField = new JTextField();
        usernameField.setBounds(140, 80, 180, 25);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(80, 130, 60, 25);
        passwordField = new JPasswordField();
        passwordField.setBounds(140, 130, 180, 25);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(100, 180, 80, 30);
        JButton exitButton = new JButton("退出");
        exitButton.setBounds(220, 180, 80, 30);

        add(titleLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(exitButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
/**
 * 重写actionPerformed方法，处理登录按钮的点击事件
 * @param e ActionEvent事件对象，包含事件的相关信息
 */
            public void actionPerformed(ActionEvent e) {
    // 获取用户名输入框中的文本，并去除前后空格
                String username = usernameField.getText().trim();
    // 获取密码输入框中的密码，将字符数组转换为字符串
                String password = new String(passwordField.getPassword());
    // 调用userService的login方法进行用户登录验证
                   User loginUser = userService.login(username, password);
    // 判断登录是否成功
                if (loginUser != null) {
        // 登录成功，关闭当前登录窗口
                    dispose();
        // 创建并显示主窗口，传入登录用户信息
                    new MainFrame(loginUser).setVisible(true);
                } else {
        // 登录失败，显示错误提示对话框
                    JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
    }
}