package com.Frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.Service.UserService;
import com.Entity.User;

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
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                User user = userService.login(username, password);
                if (user != null) {
                    new MainFrame(user).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // 正常退出程序（状态码0表示正常退出）
            }
        });
        setVisible(true);
    }
}