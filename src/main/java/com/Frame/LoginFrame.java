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
    private static UserService userService = new UserService();

    public static int userole;

    public LoginFrame() {
        // 创建登录界面
        setTitle("郑州轻工业大学仓库管理系统 - 登录");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // 取消默认布局，使用BorderLayout承载背景面板
        setLayout(new BorderLayout());

        // ---------- 1. 自定义背景面板（使用类路径加载图片） ----------
        JPanel backgroundPanel = new JPanel() {
            // 加载类路径下的背景图片（和你示例的/static/image/路径格式一致）
            // 替换img_login_bg.png为你实际的图片文件名
            private Image backgroundImage;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 初始化图片（通过类加载器读取类路径资源）
                if (backgroundImage == null) {
                    backgroundImage = new ImageIcon(getClass().getResource("/static/image/img16.png")).getImage();
                }
                // 绘制背景图，铺满整个面板
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        // 背景面板使用绝对布局，和原代码保持一致
        backgroundPanel.setLayout(null);
        // 将背景面板设为窗口的内容面板
        setContentPane(backgroundPanel);

        // ---------- 2. 原组件添加到背景面板中 ----------
        JLabel titleLabel = new JLabel("仓库管理系统");
        titleLabel.setBounds(120, 30, 200, 30);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));
        // 标题文字设为黄色，提升背景上的可读性
        titleLabel.setForeground(Color.YELLOW);

        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setBounds(80, 80, 60, 25);
        usernameLabel.setForeground(Color.BLACK);
        usernameField = new JTextField();
        usernameField.setBounds(140, 80, 180, 25);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(80, 130, 60, 25);
        passwordLabel.setForeground(Color.BLACK);
        passwordField = new JPasswordField();
        passwordField.setBounds(140, 130, 180, 25);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(100, 180, 80, 30);
        JButton exitButton = new JButton("退出");
        exitButton.setBounds(220, 180, 80, 30);

        // 将组件添加到背景面板
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(usernameLabel);
        backgroundPanel.add(usernameField);
        backgroundPanel.add(passwordLabel);
        backgroundPanel.add(passwordField);
        backgroundPanel.add(loginButton);
        backgroundPanel.add(exitButton);

        // ---------- 3. 原事件监听逻辑（保持不变） ----------
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                User loginUser = userService.login(username, password);
                // 避免空指针：loginUser为null时不获取userRole
                userole = loginUser != null ? loginUser.getUserRole() : 0;

                if (loginUser != null) {
                    dispose();
                    new MainFrame(loginUser).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
    }
}